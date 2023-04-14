package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cuijiufeng
 * @Class RolePermission
 * @Date 2023/4/14 15:57
 */
@Data
@TableName("sys_role_permission")
public class RolePermission implements Serializable {
    @TableId
    private String id;
    private String rId;
    private String pId;
}
