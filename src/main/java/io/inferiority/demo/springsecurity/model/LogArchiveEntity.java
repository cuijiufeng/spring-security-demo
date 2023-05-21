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
 * @date 2023/5/21 15:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_log_archive")
public class LogArchiveEntity implements Serializable {
    @TableId
    private String id;
    private String fileName;
    private byte[] logFile;
    private Integer fileSize;
    private Date archiveTime;
    private Integer archiveCnt;
}
