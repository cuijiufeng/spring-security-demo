package io.inferiority.demo.springsecurity.service.impl;

import io.inferiority.demo.springsecurity.model.User;
import io.inferiority.demo.springsecurity.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author cuijiufeng
 * @Class AuthLoginServiceImpl
 * @Date 2023/4/14 17:45
 */
@Service
public class AuthServiceImpl implements IAuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)) {
            //用户名密码错误
            throw new UsernameNotFoundException("用户不存在");
        }

        //将用户存入上下文中
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Override
    public void logout(User user) {
        //清除上下文
        SecurityContextHolder.clearContext();
    }
}
