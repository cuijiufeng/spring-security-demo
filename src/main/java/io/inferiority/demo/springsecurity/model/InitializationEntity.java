package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cuijiufeng
 * @Class InitializationEntity
 * @Date 2023/5/19 8:57
 */
@Data
@TableName("sys_initialization")
public class InitializationEntity implements Serializable {
    @TableId
    private String id;
    private String superUserId;
    private Boolean initialized;
}
