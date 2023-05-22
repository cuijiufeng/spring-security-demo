package io.inferiority.demo.springsecurity.service;

import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.model.JobEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import org.quartz.SchedulerException;

/**
 * @author cuijiufeng
 * @Class IJobService
 * @Date 2023/5/22 13:01
 */
public interface IJobService {
    PageInfo<JobEntity> list(PageDto page, JobEntity searchJob);

    void edit(JobEntity job) throws SchedulerException, ClassNotFoundException;
}
