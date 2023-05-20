package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.dao.LogMapper;
import io.inferiority.demo.springsecurity.model.LogEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.ILogService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

/**
 * @author cuijiufeng
 * @date 2023/5/20 16:41
 */
@Slf4j
@Service
public class LogServiceImpl implements ILogService {
    private final MessageDigest md5 = MessageDigest.getInstance("MD5");
    @Autowired
    private LogMapper logMapper;

    public LogServiceImpl() throws NoSuchAlgorithmException {
    }

    @Override
    public PageInfo<LogEntity> list(PageDto page, LogEntity searchLog, Boolean resultStatus) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true, null, page.isAll());
        List<LogEntity> logs = logMapper.selectList(Wrappers.<LogEntity>lambdaQuery()
                .likeRight(StringUtils.isNotBlank(searchLog.getOptUser()), LogEntity::getOptUser, searchLog.getOptUser())
                .eq(resultStatus != null && resultStatus, LogEntity::getResultCode, JsonResultUtil.SUCCESS_CODE)
                .ne(resultStatus != null && !resultStatus, LogEntity::getResultCode, JsonResultUtil.SUCCESS_CODE)
                .eq(searchLog.getAudited() != null, LogEntity::getAudited, searchLog.getAudited())
                .between(ArrayUtils.isNotEmpty(page.getDateRange()), LogEntity::getOptTime, page.getStart(), page.getEnd())
                .orderByDesc(LogEntity::getOptTime));
        return new PageInfo<>(logs);
    }

    @Override
    public boolean audit(String id) {
        boolean flag = false;
        LogEntity logEntity = logMapper.selectById(id);
        if (Objects.nonNull(logEntity.getAudited())) {
            return logEntity.getAudited();
        }
        try {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider()
                    .addFilter("logExcludeMacFilter", SimpleBeanPropertyFilter.serializeAllExcept("mac"));
            byte[] logBytes = new ObjectMapper()
                    .addMixIn(LogEntity.class, LogExcludeMacFilter.class)
                    .writer(filterProvider)
                    .writeValueAsBytes(logEntity);
            flag = logEntity.getMac().equals(Hex.encodeHexString(md5.digest(logBytes)));
            log.info("log mac: {}", new String(logBytes));
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage(), e);
            flag = false;
        } finally {
            logEntity.setAudited(flag);
            logMapper.updateById(logEntity);
        }
        return flag;
    }
}
