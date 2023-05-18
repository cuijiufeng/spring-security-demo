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
    @NotEmpty(message = "角色名称不能为空")
    @Size(min = 1, max = 255, message = "角色名称长度非法")
    private String roleName;
    @NotEmpty(message = "角色标识不能为空")
    @Size(min = 1, max = 255, message = "角色标识长度非法")
    private String roleKey;
    @NotNull(message = "角色等级不能为空")
    @Min(value = 1, message = "角色等级非法")
    private Integer level;
    private String createUser;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
