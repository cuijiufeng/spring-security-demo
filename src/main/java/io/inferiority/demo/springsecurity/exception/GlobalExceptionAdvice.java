package io.inferiority.demo.springsecurity.exception;

import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cuijiufeng
 * @Class GlobalExceptionAdvice
 * @Date 2023/4/14 17:39
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<JsonResult<?>> defaultErrorHandler(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(JsonResultUtil.errorJson(500, new JsonResult.ErrorData("10000", "unknown error")));
    }
}
