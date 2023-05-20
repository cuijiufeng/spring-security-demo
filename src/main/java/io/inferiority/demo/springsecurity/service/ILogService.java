package io.inferiority.demo.springsecurity.service;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.model.LogEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;

/**
 * @author cuijiufeng
 * @date 2023/5/20 16:40
 */
public interface ILogService {
    @JsonFilter("logExcludeMacFilter")
    class LogExcludeMacFilter {
    }

    PageInfo<LogEntity> list(PageDto page, LogEntity searchLog, Boolean resultStatus);

    boolean audit(String id);
}
