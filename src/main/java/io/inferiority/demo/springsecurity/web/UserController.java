package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.aop.log.Log;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.IUserService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Log("用户列表")
    @GetMapping("list")
    public JsonResult<Object> list(@Validated PageDto page, UserEntity searchUser) {
        return JsonResultUtil.successJson(userService.list(page, searchUser));
    }
}