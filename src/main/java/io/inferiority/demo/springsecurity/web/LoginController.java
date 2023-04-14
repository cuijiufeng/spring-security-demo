package io.inferiority.demo.springsecurity.web;

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
public class LoginController {

    @PostMapping("login")
    public void login() {

    }
}
