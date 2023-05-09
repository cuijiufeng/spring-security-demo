package io.inferiority.demo.springsecurity.aop.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inferiority.demo.springsecurity.dao.LogMapper;
import io.inferiority.demo.springsecurity.exception.BaseErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.LogEntity;
import io.inferiority.demo.springsecurity.utils.RequestContextUtil;
import io.inferiority.demo.springsecurity.utils.SnowflakeId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Date;

/**
 * @Class: OptLogAdvice
 * @Date: 2021/11/18 10:23
 * @auth: cuijiufeng
 */
@Slf4j
@Aspect
@Component
public class LogAdvice {
    @Value("#{T(io.inferiority.demo.springsecurity.utils.CryptoUtil).parsePublicKey('${jwt.pub.key:classpath:jwt/rsa.pub.der}')}")
    private PublicKey jwtPubKey;
    @Autowired
    private LogMapper logMapper;
    @Pointcut("@annotation(io.inferiority.demo.springsecurity.aop.log.Log)")
    public void pt() {}

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
            LogEntity logEntity = new LogEntity(SnowflakeId.generateStrId(), RequestContextUtil.currentUsername(jwtPubKey),
                    log.value(), null, null, null, new Date(), null, null);
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
            LogEntity logEntity = new LogEntity(SnowflakeId.generateStrId(), RequestContextUtil.currentUsername(jwtPubKey),
                    log.value(), 500, null, null, new Date(), null, null);
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

    private String genMac(LogEntity logEntity) throws JsonProcessingException, NoSuchAlgorithmException {
        byte[] logBytes = new ObjectMapper().writeValueAsBytes(logEntity);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Hex.encodeHexString(md5.digest(logBytes));
    }
}
