package io.inferiority.demo.springsecurity.config.security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.inferiority.demo.springsecurity.dao.PermissionMapper;
import io.inferiority.demo.springsecurity.dao.RoleMapper;
import io.inferiority.demo.springsecurity.dao.RolePermissionMapper;
import io.inferiority.demo.springsecurity.dao.UserMapper;
import io.inferiority.demo.springsecurity.model.PermissionEntity;
import io.inferiority.demo.springsecurity.model.RoleEntity;
import io.inferiority.demo.springsecurity.model.RolePermissionEntity;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.TokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author cuijiufeng
 * @Class UserDetailServiceImpl
 * @Date 2023/4/14 16:01
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TokenVo user = userMapper.selectOneTokenVo(Wrappers.<UserEntity>lambdaQuery()
                .select(UserEntity.class, i -> true)
                .eq(UserEntity::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<String> permissionIds = rolePermissionMapper.selectList(Wrappers.<RolePermissionEntity>lambdaQuery()
                .eq(RolePermissionEntity::getRId, user.getRoleId()))
                .stream()
                .map(RolePermissionEntity::getPId)
                .collect(Collectors.toList());
        List<PermissionEntity> permissions = permissionIds.isEmpty()
                ? Collections.emptyList()
                : permissionMapper.selectList(Wrappers.<PermissionEntity>lambdaQuery()
                    .eq(PermissionEntity::getType, PermissionEntity.PermissionType.PERMISSION)
                    .in(PermissionEntity::getId, permissionIds));
        List<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(PermissionEntity::getCode)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        RoleEntity role = roleMapper.selectById(user.getRoleId());
        if (Objects.nonNull(role)) {
            user.setRole(role);
            authorities.add(new SimpleGrantedAuthority(role.getRoleKey()));
        }
        user.setAuthorities(authorities);
        //返回用户
        return user;
    }
}
