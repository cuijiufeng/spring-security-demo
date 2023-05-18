package io.inferiority.demo.springsecurity.web;

import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.aop.log.Log;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.RoleEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.IRoleService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/4/16 10:50
 */
@RequestMapping("role")
@RestController
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @PreAuthorize("hasAnyAuthority('system:role:select')")
    @Log("role list")
    @GetMapping("list")
    public JsonResult<PageInfo<RoleEntity>> list(@Validated PageDto page, RoleEntity searchRole) {
        return JsonResultUtil.successJson(roleService.list(page, searchRole));
    }

    @PreAuthorize("hasAnyAuthority('system:role:add/edit')")
    @Log("add/edit role")
    @PostMapping("edit")
    public JsonResult<Void> edit(RoleEntity role) {
        roleService.edit(role);
        return JsonResultUtil.successJson();
    }

    @PreAuthorize("hasAnyAuthority('system:role:delete')")
    @Log("delete role")
    @PostMapping("delete")
    public JsonResult<Void> delete(@RequestParam List<String> ids) {
        roleService.delete(ids);
        return JsonResultUtil.successJson();
    }
}
