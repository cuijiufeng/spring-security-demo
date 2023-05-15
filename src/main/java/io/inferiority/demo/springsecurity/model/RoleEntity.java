package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
    private String roleName;
    private String roleKey;
    private String parentId;
    private Integer level;
    private String createUser;
    private Date createTime;
}
