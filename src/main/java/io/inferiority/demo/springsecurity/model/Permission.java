package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cuijiufeng
 * @Class Premission
 * @Date 2023/4/14 15:56
 */
@Data
@TableName("sys_permission")
public class Permission implements Serializable {
    @TableId
    private String id;
    private String menuCode;
    private String menuName;
    private String permissionCode;
    private String permissionName;
    private Boolean required;
}
