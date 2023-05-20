package io.inferiority.demo.springsecurity.aop.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.inferiority.demo.springsecurity.dao.LogMapper;
import io.inferiority.demo.springsecurity.exception.BaseErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.LogEntity;
import io.inferiority.demo.springsecurity.model.vo.TokenVo;
import io.inferiority.demo.springsecurity.service.ILogService;
import io.inferiority.demo.springsecurity.utils.AuthContextUtil;
import io.inferiority.demo.springsecurity.utils.JwtUtil;
import io.inferiority.demo.springsecurity.utils.SnowflakeId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

/**
 * @Class: OptLogAdvice
 * @Date: 2021/11/18 10:23
 * @auth: cuijiufeng
 */
@Slf4j
@Aspect
@Component
public class LogAdvice {
    private final ThreadLocal<Long> startCostTimeThreadLocal = new ThreadLocal<>();
    private final MessageDigest md5 = MessageDigest.getInstance("MD5");
    @Autowired
    private LogMapper logMapper;

    public LogAdvice() throws NoSuchAlgorithmException {
    }

    @Pointcut("@annotation(io.inferiority.demo.springsecurity.aop.log.Log)")
    public void pt() {}

    @Before("pt()")
    public void before() {
        startCostTimeThreadLocal.set(System.currentTimeMillis());
    }

    /**
     * 后置
     * @param joinPoint
     * @param rs
     */
    @AfterReturning(pointcut = "pt()", returning = "rs")
    public void afterReturning(JoinPoint joinPoint, Object rs) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Log log = signature.getMethod().getAnnotation(Log.class);
            LogEntity logEntity = new LogEntity(SnowflakeId.generateStrId(), getCurrentUsername(),
                    log.value(), null, null, null, new Date(),
                    System.currentTimeMillis() - startCostTimeThreadLocal.get(), null, null);
            if (rs instanceof JsonResult) {
                JsonResult<?> result = (JsonResult<?>) rs;
                logEntity.setResultCode(result.getCode());
                if (result.getData() instanceof BaseErrorEnum) {
                    BaseErrorEnum error = (BaseErrorEnum) result.getData();
                    logEntity.setErrCode(error.getCode());
                    logEntity.setErrMsg(error.getMessage());
                }
            }
            logEntity.setMac(genMac(logEntity));
            logMapper.insert(logEntity);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * 异常
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "pt()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Log log = signature.getMethod().getAnnotation(Log.class);
            LogEntity logEntity = new LogEntity(SnowflakeId.generateStrId(), getCurrentUsername(),
                    log.value(), 500, null, null, new Date(),
                    System.currentTimeMillis() - startCostTimeThreadLocal.get(), null, null);
            if (e instanceof ServiceException) {
                BaseErrorEnum error = ((ServiceException) e).getError();
                logEntity.setErrCode(error.getCode());
                logEntity.setErrMsg(error.getMessage());
            } else {
                logEntity.setErrCode("-1");
                logEntity.setErrMsg(e.getMessage());
            }
            logEntity.setMac(genMac(logEntity));
            logMapper.insert(logEntity);
        } catch (Exception ex) {
            log.warn(ex.getMessage(), ex);
        }
    }

    private String getCurrentUsername() {
        try {
            if (!"anonymousUser".equals(AuthContextUtil.currentUsername())) {
                return AuthContextUtil.currentUsername();
            }
            return getCurrentUsernameByToken();
        } catch (Exception e) {
            return getCurrentUsernameByToken();
        }
    }

    private String getCurrentUsernameByToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(JwtUtil.TOKEN_HEADER);
        if (StringUtils.isBlank(token)) {
            return null;
        }
        try {
            Map<String, Object> claims = new ObjectMapper().readValue(
                    Base64.decodeBase64(token.split("\\.")[1]),
                    new TypeReference<Map<String, Object>>() {});
            TokenVo tokenVo = new ObjectMapper().convertValue(claims.get(JwtUtil.ADDITIONAL), TokenVo.class);
            return tokenVo.getUsername();
        } catch (IOException e) {
            return null;
        }
    }

    private String genMac(LogEntity logEntity) throws JsonProcessingException {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("logExcludeMacFilter", SimpleBeanPropertyFilter.serializeAllExcept("mac"));
        byte[] logBytes = new ObjectMapper()
                .addMixIn(LogEntity.class, ILogService.LogExcludeMacFilter.class)
                .writer(filterProvider)
                .writeValueAsBytes(logEntity);
        log.info("log mac: {}", new String(logBytes));
        return Hex.encodeHexString(md5.digest(logBytes));
    }
}
