package io.inferiority.demo.springsecurity.service;

import io.inferiority.demo.springsecurity.model.UserEntity;

/**
 * @author cuijiufeng
 * @Class IAuthLogin
 * @Date 2023/4/14 17:44
 */
public interface IAuthService {

    void login(UserEntity user);

    void logout(UserEntity user);
}
