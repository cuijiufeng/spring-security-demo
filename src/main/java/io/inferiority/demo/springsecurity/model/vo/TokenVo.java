package io.inferiority.demo.springsecurity.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.inferiority.demo.springsecurity.model.RoleEntity;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.utils.JwtUtil;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author cuijiufeng
 * @date 2023/4/15 17:39
 */
@Data
public class TokenVo extends UserEntity implements UserDetails {
    private Collection<SimpleGrantedAuthority> authorities;
    private RoleEntity role;

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
