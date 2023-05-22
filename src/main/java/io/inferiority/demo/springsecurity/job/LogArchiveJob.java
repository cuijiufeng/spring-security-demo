package io.inferiority.demo.springsecurity.job;

import io.inferiority.demo.springsecurity.config.quartz.JobProperty;
import io.inferiority.demo.springsecurity.service.ILogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

/**
 * @author cuijiufeng
 * @Class LogArchiveJob
 * @Date 2023/5/22 10:04
 */
@Component
@ConditionalOnExpression("${job.enabled.log.archive:true}")
public class LogArchiveJob implements Job, JobProperty {
    @Autowired
    private ILogService logService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logService.archive(Collections.emptyList());
        } catch (IOException e) {
            throw new JobExecutionException(e);
        }
    }

    @Override
    public String getJobName() {
        return "日志归档";
    }

    @Override
    public String getJobKey() {
        return "LogArchiveJob";
    }

    @Override
    public String getTriggerKey() {
        return "LogArchiveTk";
    }

    @Override
    public String defaultCron() {
        return "0 0/1 * * * ?";
    }
}
