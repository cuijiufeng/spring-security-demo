package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.vo.SystemInfoVo;
import io.inferiority.demo.springsecurity.service.IIndexService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuijiufeng
 * @Class HomeController
 * @Date 2023/6/8 11:38
 */
@RequestMapping("index")
@RestController
public class IndexController {
    @Autowired
    private IIndexService indexService;

    @PreAuthorize("hasAnyAuthority('index:system-info')")
    @GetMapping("system-info")
    public JsonResult<SystemInfoVo> systemInfo() {
        return JsonResultUtil.successJson(indexService.systemInfo());
    }
}
