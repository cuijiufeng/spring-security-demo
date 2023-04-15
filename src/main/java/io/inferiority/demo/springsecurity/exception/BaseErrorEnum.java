package io.inferiority.demo.springsecurity.exception;

import java.io.Serializable;

/**
 * @Class: BaseErrorEnum
 * @Date: 2021/7/20 16:40
 * @auth: cuijiufeng
 */
public interface BaseErrorEnum extends Serializable {
    BaseErrorEnum UNKNOWN = new BaseErrorEnum() {
        @Override
        public String getCode() {
            return "-1";
        }
        @Override
        public String getMessage() {
            return "unknown";
        }
    };
    BaseErrorEnum UNAUTHORIZED = new BaseErrorEnum() {
        @Override
        public String getCode() {
            return "10401";
        }
        @Override
        public String getMessage() {
            return "unauthorized";
        }
    };
    BaseErrorEnum FORBIDDEN = new BaseErrorEnum() {
        @Override
        public String getCode() {
            return "10403";
        }
        @Override
        public String getMessage() {
            return "forbidden";
        }
    };

    String getCode();

    String getMessage();

    default String toLogString() {
        return "[code=" + getCode() + ",message=" + getMessage() + "]";
    }
}
