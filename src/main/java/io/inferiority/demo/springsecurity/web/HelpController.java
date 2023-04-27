package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuijiufeng
 * @Class HelpController
 * @Date 2023/4/14 15:46
 */
@RestController
@RequestMapping("help")
public class HelpController {

    @PreAuthorize("hasAnyRole('admin')")
    @GetMapping("/hello")
    public JsonResult<Object> help() {
        return JsonResultUtil.successJson("help security");
    }

    @PreAuthorize("hasAnyAuthority('help:other')")
    @GetMapping("/other")
    public JsonResult<Object> other() {
        return JsonResultUtil.successJson("help other");
    }
}
