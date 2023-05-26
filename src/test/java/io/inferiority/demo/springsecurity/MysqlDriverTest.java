package io.inferiority.demo.springsecurity;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author cuijiufeng
 * @Class MysqlDriverTest
 * @Date 2023/5/26 16:05
 */
public class MysqlDriverTest {

    @Test
    public void testMysqlDriver() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.0.36:3306/spring-security-demo?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8",
                "root", "root");
        //创建语句
        Statement st = conn.createStatement();
        //执行语句
        ResultSet rs = st.executeQuery("select * from sys_job");
        //处理结果
        while(rs.next()){
            System.out.println(rs.getObject(1)+"\t"+rs.getObject(2)+"\t"+rs.getObject(3)+"\t"+rs.getObject(4));
        }
    }
}
