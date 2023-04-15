package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.exception.BaseErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.User;
import io.inferiority.demo.springsecurity.service.IAuthService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @PostMapping("login")
    public JsonResult<Object> login(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.getFieldErrorCount("username") > 0 || bindingResult.getFieldErrorCount("password") > 0) {
            throw new ServiceException(BaseErrorEnum.UNKNOWN);
        }
        authService.login(user);
        return JsonResultUtil.successJson();
    }

    @PostMapping("logout")
    public JsonResult<Object> logout(User user) {
        if (StringUtils.isBlank(user.getId())) {
            throw new ServiceException(BaseErrorEnum.UNKNOWN);
        }
        authService.logout(user);
        return JsonResultUtil.successJson();
    }
}
