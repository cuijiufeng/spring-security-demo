package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.service.IPermissionService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuijiufeng
 * @date 2023/4/16 10:49
 */
@PreAuthorize("hasPermission()")
@RequestMapping("permission")
@RestController
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    public JsonResult<Object> all() {
        return JsonResultUtil.successJson(permissionService.allPermissions());
    }
}
