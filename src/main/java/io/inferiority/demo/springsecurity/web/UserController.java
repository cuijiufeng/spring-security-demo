package io.inferiority.demo.springsecurity.web;

import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.aop.log.Log;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.model.vo.UserVo;
import io.inferiority.demo.springsecurity.service.IUserService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import io.inferiority.demo.springsecurity.utils.ValidatedUpdate;
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
 * @date 2023/4/16 10:50
 */
@RequestMapping("user")
@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    @PreAuthorize("hasAnyAuthority('system:user:select')")
    @Log("user list")
    @GetMapping("list")
    public JsonResult<PageInfo<UserVo>> list(@Validated PageDto page, UserEntity searchUser) {
        return JsonResultUtil.successJson(userService.list(page, searchUser));
    }

    @PreAuthorize("hasAnyAuthority('system:user:add/edit')")
    @Log("add/edit user")
    @PostMapping("edit")
    public JsonResult<Void> edit(@Validated(ValidatedUpdate.class) UserEntity user,
                                 @RequestParam(name = "originalPassword", required = false) String originalPassword) {
        userService.edit(user, originalPassword);
        return JsonResultUtil.successJson();
    }
}