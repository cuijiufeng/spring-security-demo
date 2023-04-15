package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuijiufeng
 * @Class OtherController
 * @Date 2023/4/14 16:30
 */
@RestController
public class OtherController {

    @PreAuthorize("hasAnyRole('ROLE_admin')")
    @GetMapping("/hello")
    public JsonResult<Object> hello() {
        return JsonResultUtil.successJson("hello security");
    }

    @PreAuthorize("hasAnyAuthority('other:other')")
    @GetMapping("/other")
    public JsonResult<Object> other() {
        return JsonResultUtil.successJson("hello other");
    }
}
