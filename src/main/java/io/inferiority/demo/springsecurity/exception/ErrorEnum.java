package io.inferiority.demo.springsecurity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cuijiufeng
 * @date 2023/4/15 17:07
 */
@AllArgsConstructor
@Getter
public enum ErrorEnum implements BaseErrorEnum {
    ;
    private final String code;
    private final String message;
}
