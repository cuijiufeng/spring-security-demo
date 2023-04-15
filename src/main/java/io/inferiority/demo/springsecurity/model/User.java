package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author cuijiufeng
 * @Class User
 * @Date 2023/4/14 15:55
 */
@Data
@TableName("sys_user")
public class User implements Serializable, UserDetails {
    @TableId
    private String id;
    @NotEmpty(message = "用户号不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
    private PasswordIntensity passwordIntensity;
    private String roleId;
    private Date createTime;
    private Date lastLoginTime;
    private Date lastUpdatePasswordTime;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public enum  PasswordIntensity {
        LOW,MEDIUM,HIGH
    }
}
