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
    UPLOAD_FILE_FAILED("10001", "upload file failed"),
    EXIST_USER_FAILED("10002", "user already exists"),
    ADD_EDIT_USER_FAILED("10003", "add/edit user failed"),
    ORIGINAL_PASSWORD_NOT_MATCH_FAILED("10004", "original password not match"),
    CANT_MODIFY_USERNAME_FAILED("10005", "can't modify username"),
    DELETE_USER_FAILED("10006", "delete user failed"),
    ADD_EDIT_ROLE_FAILED("10007", "add/edit role failed"),
    CANT_MODIFY_ROLE_KEY_FAILED("10008", "can't modify role key"),
    DELETE_ROLE_FAILED("10009", "delete role failed"),
    DELETE_THIS_ROLE_USER_FAILED("10010", "delete this role user"),
    EXIST_ROLE_FAILED("10011", "exist role failed"),
    EDIT_PERMISSION_FAILED("10012", "edit permission failed"),
    LOG_ARCHIVE_FALIED("10013", "log archive failed")
    ;
    private final String code;
    private final String message;
}
