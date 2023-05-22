package io.inferiority.demo.springsecurity.web;

import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.aop.log.Log;
import io.inferiority.demo.springsecurity.model.JobEntity;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.IJobService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuijiufeng
 * @Class JobController
 * @Date 2023/5/22 12:58
 */
@RequestMapping("job")
@RestController
public class JobController {
    @Autowired
    private IJobService jobService;

    @Log("job list")
    @PreAuthorize("hasAnyAuthority('job:select')")
    @GetMapping("list")
    public JsonResult<PageInfo<JobEntity>> list(@Validated PageDto page, JobEntity searchJob) {
        return JsonResultUtil.successJson(jobService.list(page, searchJob));
    }

    @Log("edit job")
    @PreAuthorize("hasAnyAuthority('job:edit')")
    @PostMapping("edit")
    public JsonResult<Void> edit(@Validated JobEntity searchJob) throws SchedulerException, ClassNotFoundException {
        jobService.edit(searchJob);
        return JsonResultUtil.successJson();
    }
}
