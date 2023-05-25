package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.inferiority.demo.springsecurity.exception.BaseErrorEnum;
import io.inferiority.demo.springsecurity.utils.poi.Excel;
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
    @Excel(title = "operator user")
    private String optUser;
    @Excel(title = "operator description")
    @JsonSerialize(using = BaseErrorEnum.MessageStringJsonSerialize.class)
    private String optDesc;
    @Excel(title = "result code")
    private Integer resultCode;
    @Excel(title = "error code")
    private String errCode;
    @Excel(title = "error message")
    private String errMsg;
    @Excel(title = "operator time")
    private Date optTime;
    @Excel(title = "cost time")
    private Long costTime;
    @Excel(title = "mac")
    private String mac;
    @Excel(title = "audited status")
    private Boolean audited;
}
