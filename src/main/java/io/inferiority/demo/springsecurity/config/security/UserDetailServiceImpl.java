package io.inferiority.demo.springsecurity.config.security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.inferiority.demo.springsecurity.dao.PermissionMapper;
import io.inferiority.demo.springsecurity.dao.RoleMapper;
import io.inferiority.demo.springsecurity.dao.RolePermissionMapper;
import io.inferiority.demo.springsecurity.dao.UserMapper;
import io.inferiority.demo.springsecurity.model.Permission;
import io.inferiority.demo.springsecurity.model.Role;
import io.inferiority.demo.springsecurity.model.RolePermission;
import io.inferiority.demo.springsecurity.model.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
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
        UserVo user = userMapper.selectOne(Wrappers.<UserVo>lambdaQuery()
                .eq(UserVo::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        Role role = roleMapper.selectById(user.getRoleId());
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(Wrappers.<RolePermission>lambdaQuery()
                .eq(RolePermission::getRId, user.getRoleId()));
        List<Permission> permissions = permissionMapper.selectBatchIds(rolePermissions.stream().map(RolePermission::getPId).collect(Collectors.toList()));
        user.setAuthorities(role, permissions);
        //返回用户
        return user;
    }
}
