package io.inferiority.demo.springsecurity.config.quartz;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

/**
 * @Class: QuartzConfig
 * @Date: 2021/9/29 15:27
 * @auth: cuijiufeng
 */
@Component
public class QuartzConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(new SpringBeanJobFactory());
        return schedulerFactoryBean;
    }
}
