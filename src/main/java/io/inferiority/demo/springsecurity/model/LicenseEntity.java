package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author cuijiufeng
 * @Class LicenseEntity
 * @Date 2023/6/9 14:53
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("sys_license")
public class LicenseEntity implements Serializable {
    @TableId
    private String id;
    private String license;
}
