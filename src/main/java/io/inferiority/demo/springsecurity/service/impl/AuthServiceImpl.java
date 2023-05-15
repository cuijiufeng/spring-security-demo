package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.inferiority.demo.springsecurity.dao.PermissionMapper;
import io.inferiority.demo.springsecurity.dao.RolePermissionMapper;
import io.inferiority.demo.springsecurity.dao.UserMapper;
import io.inferiority.demo.springsecurity.model.PermissionEntity;
import io.inferiority.demo.springsecurity.model.RolePermissionEntity;
import io.inferiority.demo.springsecurity.model.UserEntity;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cuijiufeng
 * @Class AuthLoginServiceImpl
 * @Date 2023/4/14 17:45
 */
@Service
public class AuthServiceImpl implements IAuthService {
    @Value("#{T(org.springframework.boot.convert.DurationStyle).detectAndParse('${token.duration:5m}')}")
    private Duration tokenDuration;
    @Value("#{T(io.inferiority.demo.springsecurity.utils.CryptoUtil).parsePrivateKey('${jwt.priv.key:classpath:jwt/rsa.der}')}")
    private PrivateKey jwtPrivKey;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public UserVo login(UserEntity user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //将用户存入上下文中
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        //token
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        response.setHeader(JwtUtil.TOKEN_HEADER, JwtUtil.createJwt(jwtPrivKey, authenticate.getPrincipal(), tokenDuration.toMillis()));

        UserVo auth = userMapper.selectOneUserVo(Wrappers.<UserEntity>lambdaQuery()
                .select(UserEntity.class, i -> true)
                .eq(UserEntity::getUsername, user.getUsername()));
        List<String> permissionIds = rolePermissionMapper.selectList(Wrappers.<RolePermissionEntity>lambdaQuery()
                .eq(RolePermissionEntity::getRId, auth.getRoleId()))
                .stream()
                .map(RolePermissionEntity::getPId)
                .collect(Collectors.toList());
        List<PermissionEntity> permissions = permissionIds.isEmpty()
                ? Collections.emptyList()
                : permissionMapper.selectBatchIds(permissionIds);
        auth.setPermissions(permissions);
        return auth;
    }

    @Override
    public void logout(UserEntity user) {
        //清除上下文
        SecurityContextHolder.clearContext();
        //token
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        response.setHeader(JwtUtil.TOKEN_HEADER, JwtUtil.createJwt(jwtPrivKey, null, 0));
    }
}
