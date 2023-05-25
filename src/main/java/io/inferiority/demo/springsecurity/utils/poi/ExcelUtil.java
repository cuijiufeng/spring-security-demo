package io.inferiority.demo.springsecurity.utils.poi;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    public void writeData(List<T> list) throws IllegalAccessException {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        //cell样式
        //标题
        for (T t : list) {
            Row row = this.sheet.createRow(currentRow++);
            //for (int i = 0; i < entryList.size(); i++) {
            //    Map.Entry<Excel, Field> entry = entryList.get(i);
            //    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            //    cell.setCellStyle(createCellStyle(entry.getKey()));
            //    cell.setCellValue(entry.getValue().get(t));
            //}
        }
    }

    public byte[] export() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.workbook.write(baos);
        return baos.toByteArray();
    }
}
