package io.inferiority.demo.springsecurity.config.quartz;

/**
 * @author cuijiufeng
 * @Class JobName
 * @Date 2023/5/22 11:17
 */
public interface JobProperty {
    String getJobName();

    default String getJobGroupKey() {
        return null;
    }

    String getJobKey();

    default String getTriggerGroupKey() {
        return null;
    }

    String getTriggerKey();

    String defaultCron();
}
