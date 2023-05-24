package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author cuijiufeng
 * @Class Role
 * @Date 2023/4/14 15:55
 */
@Data
@TableName("sys_role")
public class RoleEntity implements Serializable {
    @TableId
    private String id;
    @NotEmpty(message = "role name can't be empty")
    @Size(min = 1, max = 255, message = "role name length illegal")
    private String roleName;
    @NotEmpty(message = "role key can't be empty")
    @Size(min = 1, max = 255, message = "role key length illegal")
    private String roleKey;
    @NotNull(message = "role level can't be null")
    @Min(value = 1, message = "role level illegal")
    private Integer level;
    private String createUser;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
