package io.inferiority.demo.springsecurity.service;

import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.model.LogArchiveEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;

/**
 * @author cuijiufeng
 * @date 2023/5/21 16:52
 */
public interface ILogArchiveService {
    PageInfo<LogArchiveEntity> list(PageDto page);

    LogArchiveEntity download(String id);
}
