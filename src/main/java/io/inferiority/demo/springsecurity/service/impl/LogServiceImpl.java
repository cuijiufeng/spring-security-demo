package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.dao.LogArchiveMapper;
import io.inferiority.demo.springsecurity.dao.LogMapper;
import io.inferiority.demo.springsecurity.exception.ErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.LogArchiveEntity;
import io.inferiority.demo.springsecurity.model.LogEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.ILogService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import io.inferiority.demo.springsecurity.utils.SnowflakeId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * @author cuijiufeng
 * @date 2023/5/20 16:41
 */
@Slf4j
@Service
public class LogServiceImpl implements ILogService {
    private final Calendar CALENDAR = Calendar.getInstance();
    private final SimpleDateFormat Y_M_D_H_M_S_S = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    private final SimpleDateFormat Y_M_D = new SimpleDateFormat("yyyy-MM-dd");
    private final MessageDigest MD5 = MessageDigest.getInstance("MD5");
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private LogArchiveMapper logArchiveMapper;

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
            flag = logEntity.getMac().equals(Hex.encodeHexString(MD5.digest(logBytes)));
            log.debug("log mac: {}", new String(logBytes));
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage(), e);
            flag = false;
        } finally {
            logEntity.setAudited(flag);
            logMapper.updateById(logEntity);
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void archive(List<String> ids) throws IOException {
        try (ByteArrayOutputStream zipBaos = new ByteArrayOutputStream();
             ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(zipBaos)) {

            Map<Date, List<LogEntity>> logs = logMapper.selectList(Wrappers.<LogEntity>lambdaQuery()
                    .in(!CollectionUtils.isEmpty(ids), LogEntity::getId, ids))
                    .stream()
                    .collect(Collectors.groupingBy(log -> {
                        CALENDAR.setTime(log.getOptTime());
                        CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
                        CALENDAR.set(Calendar.MINUTE, 0);
                        CALENDAR.set(Calendar.SECOND, 0);
                        CALENDAR.set(Calendar.MILLISECOND, 0);
                        return CALENDAR.getTime();
                    }));
            if (CollectionUtils.isEmpty(logs)) {
                return;
            }
            for (Map.Entry<Date, List<LogEntity>> entry : logs.entrySet()) {
                if (CollectionUtils.isEmpty(entry.getValue())) {
                    continue;
                }
                ZipArchiveEntry zipEntry = new ZipArchiveEntry(String.format("log-%s.log", Y_M_D.format(entry.getKey())));
                zipEntry.setMethod(ZipEntry.DEFLATED);
                zipOutputStream.putArchiveEntry(zipEntry);
                zipOutputStream.write(entry.getValue()
                        .stream()
                        .map(this::generateLogSql)
                        .collect(Collectors.joining("\n"))
                        .getBytes(StandardCharsets.UTF_8));
                zipOutputStream.closeArchiveEntry();
            }
            zipOutputStream.flush();
            zipOutputStream.finish();

            byte[] zipFile = zipBaos.toByteArray();
            if (logArchiveMapper.insert(new LogArchiveEntity(SnowflakeId.generateStrId(), "log.zip", zipFile, zipFile.length, new Date(),
                    logs.values().stream().mapToLong(Collection::size).sum())) < 1) {
                throw new ServiceException(ErrorEnum.LOG_ARCHIVE_FALIED);
            }
        }
        logMapper.delete(Wrappers.<LogEntity>lambdaQuery()
                .in(!CollectionUtils.isEmpty(ids), LogEntity::getId, ids));
    }

    private String generateLogSql(LogEntity logEntity) {
        return String.format("INSERT INTO `sys_log` VALUES ('%s', %s, '%s', %s, %s, %s, '%s', %s, '%s', %s);",
                logEntity.getId(),
                StringUtils.isBlank(logEntity.getOptUser()) ? logEntity.getOptUser() : "'" + logEntity.getOptUser() + "'",
                logEntity.getOptDesc(),
                logEntity.getResultCode(),
                StringUtils.isBlank(logEntity.getErrCode()) ? logEntity.getErrCode() : "'" + logEntity.getErrCode() + "'",
                StringUtils.isBlank(logEntity.getErrMsg()) ? logEntity.getErrMsg() : "'" + logEntity.getErrMsg() + "'",
                Y_M_D_H_M_S_S.format(logEntity.getOptTime()),
                logEntity.getCostTime(),
                logEntity.getMac(),
                logEntity.getAudited());
    }
}
