package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.inferiority.demo.springsecurity.utils.ValidatedUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
    @Size(min = 5, max = 255, message = "用户名长度非法", groups = ValidatedUpdate.class)
    @NotEmpty(message = "用户号不能为空", groups = ValidatedUpdate.class)
    private String username;
    @JsonIgnore
    @Size(min = 1, max = 255, message = "密码长度非法")
    @NotEmpty(message = "密码不能为空")
    private String password;
    private PasswordIntensity passwordIntensity;
    private String roleId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdatePasswordTime;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;
    private Boolean accountNonLocked;
    private Boolean enabled;

    @AllArgsConstructor
    @Getter
    public enum  PasswordIntensity {
        LOW("L"), MEDIUM("M"), HIGH("H");
        //标记数据库存的值是枚举
        @EnumValue
        private final String code;
    }
}
