package io.inferiority.demo.springsecurity.exception;

import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author cuijiufeng
 * @Class GlobalExceptionAdvice
 * @Date 2023/4/14 17:39
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
    public void authenticationException(Exception e) throws Exception {
        //security exception交给security filter处理
        throw e;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResult<?>> defaultErrorHandler(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(JsonResultUtil.UNKNOWN);
    }
}
