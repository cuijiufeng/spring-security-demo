package io.inferiority.demo.springsecurity;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.inferiority.demo.springsecurity.config.quartz.JobProperty;
import io.inferiority.demo.springsecurity.config.quartz.QuartzManager;
import io.inferiority.demo.springsecurity.dao.JobMapper;
import io.inferiority.demo.springsecurity.model.JobEntity;
import io.inferiority.demo.springsecurity.utils.SnowflakeId;
import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author cuijiufeng
 * @Class ApplicationPostRunner
 * @Date 2023/5/22 10:11
 */
@Order(1)
@Component
public class InitJobApplicationRunner implements ApplicationRunner {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private QuartzManager quartzManager;
    @Autowired
    private JobMapper jobMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, JobProperty> jobBeans = applicationContext.getBeansOfType(JobProperty.class);
        //数据库中不存在时
        for (Map.Entry<String, JobProperty> jobBean : jobBeans.entrySet()) {
            JobProperty jobProperty = jobBean.getValue();
            try {
                jobMapper.insert(new JobEntity(SnowflakeId.generateStrId(), jobProperty.getJobName(), jobProperty.getClass().getName(),
                        jobProperty.getJobGroupKey(), jobProperty.getJobKey(),
                        jobProperty.getTriggerGroupKey(), jobProperty.getTriggerKey(),
                        jobProperty.defaultCron(), false));
            } catch (Exception ignore) {
            }
        }
        List<JobEntity> jobs = jobMapper.selectList(Wrappers.<JobEntity>lambdaQuery()
                .eq(JobEntity::getStarted, true));
        for (JobEntity job : jobs) {
            //noinspection unchecked
            quartzManager.scheduleJob((Class<? extends Job>) Class.forName(job.getJobClass()),
                    job.getJobGroupKey(), job.getJobKey(),
                    job.getTkGroupKey(), job.getTkKey(), job.getCron());
        }
    }
}
