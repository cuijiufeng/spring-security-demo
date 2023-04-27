package io.inferiority.demo.springsecurity.exception;

import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * @author cuijiufeng
 * @Class GlobalExceptionAdvice
 * @Date 2023/4/14 17:39
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 业务异常
     * @param e
     * @return org.springframework.http.ResponseEntity<io.inferiority.demo.springsecurity.model.JsonResult<?>>
     * @throws
    */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<JsonResult<?>> serviceException(ServiceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(JsonResultUtil.errorJson(500, e.getError()));
    }

    /**
     * validate参数绑定异常
     * @param e
     * @return org.springframework.http.ResponseEntity<java.lang.String>
     * @throws
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<JsonResult<?>> bindException(BindException e) {
        log.warn(e.getMessage());
        String errMsg = e.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(JsonResultUtil.errorJson(500, BaseErrorEnum.BUILD.apply("-1", errMsg)));
    }

    /**
     * spring security异常交给security filter处理
     * @param e
     * @return void
     * @throws
    */
    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
    public void authenticationException(Exception e) throws Exception {
        throw e;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResult<?>> defaultErrorHandler(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(JsonResultUtil.UNKNOWN);
    }
}
