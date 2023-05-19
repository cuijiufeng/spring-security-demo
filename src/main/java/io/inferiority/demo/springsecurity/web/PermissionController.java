package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.aop.log.Log;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.vo.PermissionVo;
import io.inferiority.demo.springsecurity.service.IPermissionService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/4/16 10:49
 */
@RequestMapping("permission")
@RestController
public class PermissionController {
    @Autowired
    private IPermissionService permissionService;

    @Log("permission tree")
    @PreAuthorize("hasAnyAuthority('system:permission:tree')")
    @GetMapping("/tree")
    public JsonResult<List<PermissionVo>> tree() {
        return JsonResultUtil.successJson(permissionService.permissionsTree("0"));
    }

    @Log("role have permission")
    @PreAuthorize("hasAnyAuthority('system:permission:have')")
    @GetMapping("/have")
    public JsonResult<List<String>> have(String roleId) {
        return JsonResultUtil.successJson(permissionService.have(roleId));
    }
}
