package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author cuijiufeng
 * @Class Log
 * @Date 2023/4/27 16:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_log")
public class LogEntity implements Serializable {
    @TableId
    private String id;
    //private String params;
    private String optUser;
    private String optDesc;
    private Integer resultCode;
    private String errCode;
    private String errMsg;
    private Date optTime;
    private String mac;
    private Boolean audited;
}
