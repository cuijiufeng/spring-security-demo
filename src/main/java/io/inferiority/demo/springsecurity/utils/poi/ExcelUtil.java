package io.inferiority.demo.springsecurity.utils.poi;

import io.inferiority.demo.springsecurity.config.ApplicationContextHolder;
import io.inferiority.demo.springsecurity.utils.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author cuijiufeng
 * @Class ExeclUtil
 * @Date 2023/5/25 10:50
 */
@Slf4j
public class ExcelUtil<T> {
    private final Workbook workbook;
    private Sheet sheet;
    private int currentRow = 0;

    public ExcelUtil() {
        //TODO 2023/5/25 11:11 每sheet最大行数
        this.workbook = new SXSSFWorkbook();
    }

    public ExcelUtil(InputStream is) throws IOException {
        this.workbook = WorkbookFactory.create(is);
    }

    public void createSheet(String sheetName) {
        this.sheet = this.workbook.createSheet(sheetName);
    }

    public void writeData(List<T> list) throws ReflectiveOperationException {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Map.Entry<Excel, Method>> entryList = getBeanExcelProperty(list.get(0).getClass(), Excel.Type.EXPORT);
        writeHeadRow(entryList.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
        for (T t : list) {
            Row row = this.sheet.createRow(currentRow++);
            for (int i = 0; i < entryList.size(); i++) {
                Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Map.Entry<Excel, Method> entry = entryList.get(i);
                //cell样式
                cell.setCellStyle(createCellStyle(this.workbook, entry.getKey()));
                setCellValue(cell, entry, ReflectUtil.invokeMethod(entry.getValue(), t));
            }
        }
    }

    private void writeHeadRow(List<Excel> excels) {
        MessageSource messageSource = ApplicationContextHolder.getApplicationContext().getBean(MessageSource.class);
        //标题
        Row headRow = this.sheet.createRow(currentRow++);
        for (int i = 0; i < excels.size(); i++) {
            Cell cell = headRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(excels.get(i).title());
            cell.setCellValue(messageSource.getMessage(excels.get(i).title(), null, LocaleContextHolder.getLocale()));
        }
    }

    private CellStyle createCellStyle(Workbook workbook, Excel key) {
        CellStyle cellStyle = workbook.createCellStyle();
        return cellStyle;
    }

    private void setCellValue(Cell cell, Map.Entry<Excel, Method> entry, Object value) {
        if (Objects.isNull(value)) {
            cell.setCellValue(entry.getKey().nullOut());
        }
        //基本数据类型
        if (value instanceof Date) {
            Date date = (Date) value;
            cell.setCellValue(new SimpleDateFormat(entry.getKey().dateFormat()).format(date));
            return;
        }
        cell.setCellValue(value.toString());
    }

    public byte[] export() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.workbook.write(baos);
        return baos.toByteArray();
    }

    private List<Map.Entry<Excel, Method>> getBeanExcelProperty(Class<?> beanClazz, Excel.Type type) {
        return ReflectUtil.getAnnotationFields(beanClazz, Excel.class)
                .filter(f -> ArrayUtils.contains(f.getAnnotation(Excel.class).type(), type))
                .sorted(Comparator.comparingInt(f -> f.getAnnotation(Excel.class).order()))
                .map(f -> new AbstractMap.SimpleEntry<>(f.getAnnotation(Excel.class),
                        type == Excel.Type.EXPORT ? ReflectUtil.getGetter(beanClazz, f) : ReflectUtil.getSetter(beanClazz, f)))
                .collect(Collectors.toList());
    }
}
