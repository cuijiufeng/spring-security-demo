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
import javax.validation.constraints.Pattern;
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
    @Size(min = 5, max = 255, message = "username length illegal", groups = ValidatedUpdate.class)
    @NotEmpty(message = "username can't be empty", groups = ValidatedUpdate.class)
    private String username;
    @JsonIgnore
    @Size(min = 1, max = 255, message = "password length illegal")
    @NotEmpty(message = "password can't be empty")
    private String password;
    private PasswordIntensity passwordIntensity;
    private String roleId;
    private Sex sex;
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$|^$",
            message = "phone number format illegal", groups = ValidatedUpdate.class)
    private String phoneNumber;
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

    @AllArgsConstructor
    @Getter
    public enum Sex {
        MALE("M"), FEMALE("F"), UNKNOWN("U");
        @EnumValue
        private final String code;
    }
}
