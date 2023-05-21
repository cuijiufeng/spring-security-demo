package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.dao.LogArchiveMapper;
import io.inferiority.demo.springsecurity.model.LogArchiveEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.ILogArchiveService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/5/21 16:52
 */
@Service
public class LogArchiveServiceImpl implements ILogArchiveService {
    @Autowired
    private LogArchiveMapper logArchiveMapper;

    @Override
    public PageInfo<LogArchiveEntity> list(PageDto page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true, null, page.isAll());
        List<LogArchiveEntity> logs = logArchiveMapper.selectList(Wrappers.<LogArchiveEntity>lambdaQuery()
                .select(LogArchiveEntity.class, info -> !info.getColumn().equals("log_file"))
                .between(ArrayUtils.isNotEmpty(page.getDateRange()), LogArchiveEntity::getArchiveTime, page.getStart(), page.getEnd())
                .orderByDesc(LogArchiveEntity::getArchiveTime));
        return new PageInfo<>(logs);
    }

    @Override
    public LogArchiveEntity download(String id) {
        return logArchiveMapper.selectById(id);
    }
}
