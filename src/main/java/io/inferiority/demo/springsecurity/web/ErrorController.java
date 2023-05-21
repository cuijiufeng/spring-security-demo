package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: hxy
 * @description: 系统错误拦截, 主要是针对404的错误
 * @date: 2017/10/24 10:31
 */
@Slf4j
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorController extends AbstractErrorController {
    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping
    public ResponseEntity<?> error(HttpServletRequest request) {
        log.error("{}", getErrorAttributes(request, ErrorAttributeOptions.defaults().including(ErrorAttributeOptions.Include.MESSAGE)));
        return ResponseEntity.ok(JsonResultUtil.NOT_FOUND);
    }
}

