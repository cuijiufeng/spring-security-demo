package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author cuijiufeng
 * @Class RolePermission
 * @Date 2023/4/14 15:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_permission")
public class RolePermissionEntity implements Serializable {
    @TableId
    private String id;
    private String rId;
    private String pId;
}
