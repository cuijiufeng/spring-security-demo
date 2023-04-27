package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author cuijiufeng
 * @Class User
 * @Date 2023/4/14 15:55
 */
@Data
@TableName("sys_user")
public class UserEntity implements Serializable {
    @TableId
    private String id;
    @NotEmpty(message = "用户号不能为空")
    private String username;
    @JsonIgnore
    @NotEmpty(message = "密码不能为空")
    private String password;
    private PasswordIntensity passwordIntensity;
    private String roleId;
    private Date createTime;
    private Date lastLoginTime;
    private Date lastUpdatePasswordTime;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;
    private Boolean accountNonLocked;
    private Boolean enabled;

    @AllArgsConstructor
    @Getter
    public enum  PasswordIntensity {
        LOW(1), MEDIUM(2), HIGH(3);
        //标记数据库存的值是枚举
        @EnumValue
        private final int code;
    }
}
