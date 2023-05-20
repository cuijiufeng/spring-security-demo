package io.inferiority.demo.springsecurity.web;

import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.LogEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.ILogService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuijiufeng
 * @date 2023/5/20 16:39
 */
@RequestMapping("log")
@RestController
public class LogController {
    @Autowired
    private ILogService logService;

    @PreAuthorize("hasAnyAuthority('log:select')")
    @GetMapping("list")
    public JsonResult<PageInfo<LogEntity>> list(@Validated PageDto page, LogEntity searchLog,
                                                @RequestParam(value = "resultStatus", required = false) Boolean resultStatus) {
        return JsonResultUtil.successJson(logService.list(page, searchLog, resultStatus));
    }

    @PreAuthorize("hasAnyAuthority('log:audit')")
    @PostMapping("audit")
    public JsonResult<Boolean> audit(@RequestParam("id") String id) {
        return JsonResultUtil.successJson(logService.audit(id));
    }
}
