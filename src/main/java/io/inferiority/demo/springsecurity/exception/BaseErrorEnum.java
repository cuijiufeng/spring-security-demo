package io.inferiority.demo.springsecurity.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.function.BiFunction;

/**
 * @Class: BaseErrorEnum
 * @Date: 2021/7/20 16:40
 * @auth: cuijiufeng
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public interface BaseErrorEnum extends Serializable {
    BiFunction<String, String, BaseErrorEnum> BUILD = (c, m) -> new BaseErrorEnum() {
        @Override
        public String getCode() {
            return c;
        }
        @Override
        public String getMessage() {
            return m;
        }
    };

    String getCode();

    String getMessage();

    default String toLogString() {
        return "[code=" + getCode() + ", message=" + getMessage() + "]";
    }
}
