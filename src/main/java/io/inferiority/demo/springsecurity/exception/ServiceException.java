package io.inferiority.demo.springsecurity.exception;

import lombok.Getter;

/**
 * @author cuijiufeng
 * @date 2023/4/15 17:00
 */
@Getter
public class ServiceException extends RuntimeException {
    private final BaseErrorEnum error;

    public ServiceException(BaseErrorEnum error) {
        super(error.toLogString());
        this.error = error;
    }

    public ServiceException(BaseErrorEnum error, Throwable cause) {
        super(error.toLogString(), cause);
        this.error = error;
    }

    @Override
    public String toString() {
        String message = super.getMessage();
        return getClass().getName() + (message != null ? ": " + message : "");
    }
}
