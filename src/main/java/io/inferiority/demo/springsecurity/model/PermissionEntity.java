package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author cuijiufeng
 * @Class Premission
 * @Date 2023/4/14 15:56
 */
@Data
@TableName("sys_permission")
public class PermissionEntity implements Serializable {
    @TableId
    private String id;
    private String parentId;
    private String type;
    private String name;
    private String code;
    private Boolean required;

    @AllArgsConstructor
    @Getter
    public enum PermissionType {
        DIRECTORY("D"), MENU("M"), PERMISSION("P");
        //标记数据库存的值是枚举
        @EnumValue
        private final String code;
    }
}
