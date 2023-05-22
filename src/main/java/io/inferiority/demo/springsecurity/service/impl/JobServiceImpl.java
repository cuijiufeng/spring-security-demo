package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.config.quartz.QuartzManager;
import io.inferiority.demo.springsecurity.dao.JobMapper;
import io.inferiority.demo.springsecurity.exception.ErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.JobEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.IJobService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author cuijiufeng
 * @Class JobServiceImpl
 * @Date 2023/5/22 13:01
 */
@Service
public class JobServiceImpl implements IJobService {
    @Autowired
    private QuartzManager quartzManager;
    @Autowired
    private JobMapper jobMapper;

    @Override
    public PageInfo<JobEntity> list(PageDto page, JobEntity searchJob) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true, null, page.isAll());
        List<JobEntity> jobs = jobMapper.selectList(Wrappers.<JobEntity>lambdaQuery()
                .likeRight(StringUtils.isNotBlank(searchJob.getJobName()), JobEntity::getJobName, searchJob.getJobName())
                .eq(Objects.nonNull(searchJob.getStarted()), JobEntity::getStarted, searchJob.getStarted()));
        return new PageInfo<>(jobs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(JobEntity job) throws SchedulerException, ClassNotFoundException {
        job.setJobClass(null);
        job.setJobGroupKey(null);
        job.setJobKey(null);
        job.setTkGroupKey(null);
        job.setTkKey(null);
        if (jobMapper.updateById(job) < 1) {
            throw new ServiceException(ErrorEnum.EDIT_JOB_FAILED);
        }
        JobEntity oldJob = jobMapper.selectById(job.getId());
        quartzManager.modifyJob(oldJob.getTkGroupKey(), oldJob.getTkKey(), job.getCron());
        if (oldJob.getStarted() != quartzManager.isJobRunning(oldJob.getTkGroupKey(), oldJob.getTkKey())) {
            if (oldJob.getStarted()) {
                //noinspection unchecked
                quartzManager.scheduleJob((Class<? extends Job>) Class.forName(oldJob.getJobClass()),
                        oldJob.getJobGroupKey(), oldJob.getJobKey(),
                        oldJob.getTkGroupKey(), oldJob.getTkKey(), oldJob.getCron());
            } else {
                quartzManager.unscheduleJob(
                        oldJob.getTkGroupKey(), oldJob.getTkKey());
            }
        }
    }
}
