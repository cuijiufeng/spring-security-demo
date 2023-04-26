package io.inferiority.demo.springsecurity.config.security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.inferiority.demo.springsecurity.dao.PermissionMapper;
import io.inferiority.demo.springsecurity.dao.RoleMapper;
import io.inferiority.demo.springsecurity.dao.RolePermissionMapper;
import io.inferiority.demo.springsecurity.dao.UserMapper;
import io.inferiority.demo.springsecurity.model.Permission;
import io.inferiority.demo.springsecurity.model.Role;
import io.inferiority.demo.springsecurity.model.RolePermission;
import io.inferiority.demo.springsecurity.model.User;
import io.inferiority.demo.springsecurity.model.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        UserVo user = userMapper.selectOneVo(Wrappers.<User>lambdaQuery()
                .select(User.class, i -> true)
                .eq(User::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        Role role = roleMapper.selectById(user.getRoleId());
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(Wrappers.<RolePermission>lambdaQuery()
                .eq(RolePermission::getRId, user.getRoleId()));
        List<Permission> permissions = permissionMapper.selectBatchIds(rolePermissions.stream().map(RolePermission::getPId).collect(Collectors.toList()));
        List<SimpleGrantedAuthority> authorities = Stream.concat(
                Stream.of(role).map(Role::getName).map(SimpleGrantedAuthority::new),
                permissions.stream().map(p -> p.getMenuCode() + ":" + p.getPermissionCode()).map(SimpleGrantedAuthority::new)).collect(Collectors.toList());
        user.setAuthorities(authorities);
        //返回用户
        return user;
    }
}
