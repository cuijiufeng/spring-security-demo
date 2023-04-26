package io.inferiority.demo.springsecurity.service;

import io.inferiority.demo.springsecurity.model.User;

/**
 * @author cuijiufeng
 * @Class IAuthLogin
 * @Date 2023/4/14 17:44
 */
public interface IAuthService {

    void login(User user);

    void logout(User user);
}
