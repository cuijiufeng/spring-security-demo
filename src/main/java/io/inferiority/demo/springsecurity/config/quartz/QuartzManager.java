package io.inferiority.demo.springsecurity.config.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @Class: QuartzManager
 * @Date: 2021/9/29 15:28
 * @auth: cuijiufeng
 */
@Slf4j
@Component
public class QuartzManager {
    private Scheduler scheduler;

    @Autowired
    public void setScheduler(SchedulerFactoryBean schedulerFactoryBean) {
        this.scheduler = schedulerFactoryBean.getScheduler();
    }

    public void scheduleJob(Class<? extends Job> jobClass, @Nullable String jobGroupName, @NonNull String jobName,
                            @Nullable String triggerGroupName, @NonNull String triggerName, String cronExp) throws SchedulerException {
        //创建JobDetail实例，并与PrintJob类绑定(Job执行内容)
        JobDetail jobDetail = JobBuilder
                .newJob(jobClass)
                .withIdentity(jobName, jobGroupName)
                .build();

        // 定义Trigger,使用Cron表达式来控制运行
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(triggerName, triggerGroupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build();

        // 将jobDetail和trigger这个调度器(注册 任务详情和触发器)
        scheduler.scheduleJob(jobDetail, trigger);
        // 启动scheduler
        if (!scheduler.isStarted()) {
            scheduler.start();
        }
    }

    public void unscheduleJob(@Nullable String triggerGroupName, @NonNull String triggerName) throws SchedulerException {
        TriggerKey tk = new TriggerKey(triggerName, triggerGroupName);
        // 停止触发器
        scheduler.pauseTrigger(tk);
        // 移除触发器
        scheduler.unscheduleJob(tk);
    }

    public boolean isJobRunning(@Nullable String triggerGroupName, @NonNull String triggerName) throws SchedulerException {
        TriggerKey tk = new TriggerKey(triggerName, triggerGroupName);
        Trigger.TriggerState triggerState = scheduler.getTriggerState(tk);
        return Trigger.TriggerState.NORMAL == triggerState;
    }

    public void modifyJob(@Nullable String triggerGroupName, @NonNull String triggerName, String cronExp) throws SchedulerException {
        TriggerKey tk = new TriggerKey(triggerName, triggerGroupName);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(tk);

        // 1.判断触发器是否存在
        // 2.比对表达式是否相同
        if (trigger == null || trigger.getCronExpression().equalsIgnoreCase(cronExp)) {
            return;
        }
        // 重启
        scheduler.rescheduleJob(tk, TriggerBuilder
                .newTrigger()
                .withIdentity(triggerName, triggerGroupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build());
        log.debug("reschedule job: {}", cronExp);
    }
}
