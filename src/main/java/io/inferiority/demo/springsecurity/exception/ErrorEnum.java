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
    UPLOAD_FILE_FAILED("10000", "upload file failed"),
    EXIST_USER_FAILED("10001", "user already exists"),
    ADD_EDIT_USER_FAILED("10002", "add/edit user failed"),
    ;
    private final String code;
    private final String message;
}
