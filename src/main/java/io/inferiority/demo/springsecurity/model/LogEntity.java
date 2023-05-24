package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.inferiority.demo.springsecurity.exception.BaseErrorEnum;
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
@JsonPropertyOrder({"id", "optUser", "optDesc", "resultCode", "errCode", "errMsg", "optTime", "costTime", "costTime", "mac", "audited"})
@TableName("sys_log")
public class LogEntity implements Serializable {
    @TableId
    private String id;
    //private String params;
    private String optUser;
    @JsonSerialize(using = BaseErrorEnum.MessageStringJsonSerialize.class)
    private String optDesc;
    private Integer resultCode;
    private String errCode;
    private String errMsg;
    private Date optTime;
    private Long costTime;
    private String mac;
    private Boolean audited;
}
