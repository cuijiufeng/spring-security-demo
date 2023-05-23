package io.inferiority.demo.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @Class: SpringSecurityDemoApplication
 * @Date: 2023/4/28 10:21
 * @author: cuijiufeng
 */
@EnableCaching
@SpringBootApplication
public class SpringSecurityDemoApplication {

    // TODO: 2023/5/21 国际化
    // TODO: 2023/5/21 缓存
    //TODO 2023/5/22 验证码
    // TODO: 2023/5/21 execl导入导出
    // TODO: 2023/5/21 首页 --> low
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoApplication.class, args);
    }

}
