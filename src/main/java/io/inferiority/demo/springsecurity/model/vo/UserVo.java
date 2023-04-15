package io.inferiority.demo.springsecurity.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.inferiority.demo.springsecurity.model.Permission;
import io.inferiority.demo.springsecurity.model.Role;
import io.inferiority.demo.springsecurity.model.User;
import io.inferiority.demo.springsecurity.utils.JwtUtil;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author cuijiufeng
 * @date 2023/4/15 17:39
 */
@TableName("sys_user")
public class UserVo extends User implements UserDetails {
    @TableField(exist = false)
    private Collection<SimpleGrantedAuthority> authorities;

    public void setAuthorities(Role role, List<Permission> permissions) {
        authorities = Stream.of(role).map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        authorities.addAll(permissions.stream().map(p -> p.getMenuCode() + ":" + p.getPermissionCode())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    @JsonSerialize(contentUsing = JwtUtil.UserVoJsonSerialize.class)
    @JsonDeserialize(contentUsing = JwtUtil.UserVoJsonDeserialize.class)
    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }
}
