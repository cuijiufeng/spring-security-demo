package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.aop.log.Log;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.UserVo;
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

    @Log("login")
    @PostMapping("login")
    public JsonResult<UserVo> login(@Validated UserEntity user) {
        return JsonResultUtil.successJson(authService.login(user));
    }

    @Log("logout")
    @PostMapping("logout")
    public JsonResult<Void> logout(UserEntity user) {
        authService.logout(user);
        return JsonResultUtil.successJson();
    }
}
