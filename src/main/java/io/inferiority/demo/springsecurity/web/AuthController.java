package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.aop.log.Log;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.service.IAuthService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuijiufeng
 * @Class LoginController
 * @Date 2023/4/14 14:18
 */
@RequestMapping("auth")
@RestController
public class AuthController {
    @Autowired
    private IAuthService authService;

    @Log("登录")
    @PostMapping("login")
    public JsonResult<Object> login(@Validated UserEntity user) {
        authService.login(user);
        return JsonResultUtil.successJson();
    }

    @Log("登出")
    @PostMapping("logout")
    public JsonResult<Object> logout(UserEntity user) {
        authService.logout(user);
        return JsonResultUtil.successJson();
    }
}
