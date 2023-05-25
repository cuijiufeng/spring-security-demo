package io.inferiority.demo.springsecurity;

import io.inferiority.demo.springsecurity.model.LogEntity;
import io.inferiority.demo.springsecurity.utils.SnowflakeId;
import io.inferiority.demo.springsecurity.utils.poi.ExcelUtil;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author cuijiufeng
 * @Class ExcelTest
 * @Date 2023/5/25 11:35
 */
public class ExcelTest {

    @Test
    public void testExcel() throws IOException, IllegalAccessException {
        List<LogEntity> logs = Collections.singletonList(new LogEntity(SnowflakeId.generateStrId(), "admin", "list",
                200, null, null, new Date(), 20L, "fjdslf", null));
        ExcelUtil<LogEntity> excelUtil = new ExcelUtil<>();
        excelUtil.createSheet("log");
        excelUtil.writeData(logs);
        IOUtils.write(excelUtil.export(), new FileOutputStream("E:\\Desktop\\logs.xls"));
    }
}
