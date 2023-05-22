package io.inferiority.demo.springsecurity.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author cuijiufeng
 * @Class JobEntity
 * @Date 2023/5/22 11:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_job")
public class JobEntity implements Serializable {
    @TableId
    private String id;
    @NotEmpty(message = "任务名为空")
    private String jobName;
    @JsonIgnore
    private String jobClass;
    private String jobGroupKey;
    private String jobKey;
    private String tkGroupKey;
    private String tkKey;
    @NotEmpty(message = "cron表达式为空")
    private String cron;
    @NotNull(message = "启动状态为空")
    private Boolean started;
}
