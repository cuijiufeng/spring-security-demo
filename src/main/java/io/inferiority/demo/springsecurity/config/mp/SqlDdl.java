package io.inferiority.demo.springsecurity.config.mp;

import com.baomidou.mybatisplus.extension.ddl.SimpleDdl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author cuijiufeng
 * @Class SqlDdl
 * @Date 2023/4/27 10:16
 */
@Component
public class SqlDdl extends SimpleDdl {
    @Value("${ddl.sql.files:db/schema.sql,db/data.sql}")
    private List<String> sqlFiles;

    @Override
    public List<String> getSqlFiles() {
        return sqlFiles;
    }
}
