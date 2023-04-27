package io.inferiority.demo.springsecurity.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.utils.JwtUtil;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author cuijiufeng
 * @date 2023/4/15 17:39
 */
@Setter
@TableName("sys_user")
public class UserVo extends UserEntity implements UserDetails {
    @TableField(exist = false)
    private Collection<SimpleGrantedAuthority> authorities;

    @JsonSerialize(contentUsing = JwtUtil.UserVoJsonSerialize.class)
    @JsonDeserialize(contentUsing = JwtUtil.UserVoJsonDeserialize.class)
    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }
}
