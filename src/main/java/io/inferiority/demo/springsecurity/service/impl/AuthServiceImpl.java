package io.inferiority.demo.springsecurity.service.impl;

import io.inferiority.demo.springsecurity.model.vo.UserVo;
import io.inferiority.demo.springsecurity.service.IAuthService;
import io.inferiority.demo.springsecurity.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.security.PrivateKey;
import java.time.Duration;

/**
 * @author cuijiufeng
 * @Class AuthLoginServiceImpl
 * @Date 2023/4/14 17:45
 */
@Service
public class AuthServiceImpl implements IAuthService {
    @Value("${token.header:Authorization}")
    private String tokenHeader;
    @Value("#{T(org.springframework.boot.convert.DurationStyle).detectAndParse('${token.duration:5m}')}")
    private Duration tokenDuration;
    @Value("#{T(io.inferiority.demo.springsecurity.utils.RsaKeyUtil).parsePrivateKey('${jwt.priv.key:classpath:rsa.der}')}")
    private PrivateKey jwtPrivKey;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void login(UserVo user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //将用户存入上下文中
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        //token
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        response.setHeader(tokenHeader, JwtUtil.createJwt(jwtPrivKey, authenticate.getPrincipal(), tokenDuration.toMillis()));
    }

    @Override
    public void logout(UserVo user) {
        //清除上下文
        SecurityContextHolder.clearContext();
        //token
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        response.setHeader(tokenHeader, JwtUtil.createJwt(jwtPrivKey, null, 0));
    }
}
