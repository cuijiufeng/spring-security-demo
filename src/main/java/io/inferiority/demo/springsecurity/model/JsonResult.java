package io.inferiority.demo.springsecurity.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Class: JsonResult
 * @Date: 2022/1/19 16:04
 * @auth: cuijiufeng
 */
@Data
public class JsonResult<T> implements Serializable {
    private Integer code;
    private T data;

    public JsonResult() {
    }

    public JsonResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }
}
