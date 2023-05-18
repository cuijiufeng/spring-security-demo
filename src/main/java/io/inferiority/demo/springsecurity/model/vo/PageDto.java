package io.inferiority.demo.springsecurity.model.vo;

import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author cuijiufeng
 * @date 2023/5/15 22:52
 */
@Data
public class PageDto {
    @Range(min = 1, max = Integer.MAX_VALUE, message = "页数范围错误")
    private int pageNum = 1;
    @Range(min = 0, max = Integer.MAX_VALUE, message = "分页大小错误")
    private int pageSize = 10;
    private boolean all = false;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date[] dateRange = new Date[0];

    public Date getStart() {
        return ArrayUtils.isNotEmpty(dateRange) ? dateRange[0] : null;
    }

    public Date getEnd() {
        return ArrayUtils.isNotEmpty(dateRange) ? dateRange[1] : null;
    }
}
