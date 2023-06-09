package io.inferiority.demo.springsecurity;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * @Class: SpringSecurityDemoApplication
 * @Date: 2023/4/28 10:21
 * @author: cuijiufeng
 */
@EnableCaching
@SpringBootApplication
public class SpringSecurityDemoApplication {

    // TODO: 2023/5/21 execl导入导出
    // TODO: 2023/5/21 首页 --> low
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoApplication.class, args);
    }

    @Bean
    public SnowflakeGenerator snowflakeGenerator() {
        return new SnowflakeGenerator();
    }
}
