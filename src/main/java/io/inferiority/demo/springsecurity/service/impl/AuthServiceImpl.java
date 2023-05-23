package io.inferiority.demo.springsecurity.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.inferiority.demo.springsecurity.config.Constants;
import io.inferiority.demo.springsecurity.dao.PermissionMapper;
import io.inferiority.demo.springsecurity.dao.RoleMapper;
import io.inferiority.demo.springsecurity.dao.RolePermissionMapper;
import io.inferiority.demo.springsecurity.dao.UserMapper;
import io.inferiority.demo.springsecurity.exception.ErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.PermissionEntity;
import io.inferiority.demo.springsecurity.model.RolePermissionEntity;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.UserVo;
import io.inferiority.demo.springsecurity.service.IAuthService;
import io.inferiority.demo.springsecurity.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
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
import java.util.Objects;
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
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private CacheManager cacheManager;

    @Override
    public UserVo login(UserEntity user, String figerprint, String verifyCode) {
        String cacheVerifyCode = cacheManager.getCache(Constants.CACHE_KEY)
                .get(Constants.CACHE_KEY_PREFIX_VERIFY_CODE + ":" + figerprint, String.class);
        if (Objects.isNull(cacheVerifyCode) || !cacheVerifyCode.equalsIgnoreCase(verifyCode)) {
            throw new ServiceException(ErrorEnum.VERIFY_CODE_NO_MATCH_FAILED);
        }
        cacheManager.getCache(Constants.CACHE_KEY)
                .evict(Constants.CACHE_KEY_PREFIX_VERIFY_CODE + ":" + figerprint);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //将用户存入上下文中
        //TODO 2023/5/17 17:51 为扩展证书登录开放接口
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
        auth.setRole(roleMapper.selectById(auth.getRoleId()));
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

    @Override
    public String verifyCode(String figerprint) {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 32);
        cacheManager.getCache(Constants.CACHE_KEY)
                .put(Constants.CACHE_KEY_PREFIX_VERIFY_CODE + ":" + figerprint, captcha.getCode());
        return captcha.getImageBase64();
    }
}
