package io.inferiority.demo.springsecurity.utils;

import io.inferiority.demo.springsecurity.model.JsonResult;

/**
 * @ClassName: JsonResultUtils
 * @Date: 2021/1/6 13:05
 * @author: cjf
 */
public class JsonResultUtil {
    /**
     * 返回成功，无交互数据
     * @return java.lang.String json串
     */
    public static JsonResult<Object> successJson() {
        return new JsonResult<>(200, null);
    }

    /**
     * 返回成功
     * @param obj 交互数据
     * @return java.lang.String json串
     */
    public static JsonResult<Object> successJson(Object obj) {
        return new JsonResult<>(200, obj);
    }

    /**
     * @param code
     * @param error
     * @return io.inferiority.demo.springsecurity.model.JsonResult<io.inferiority.demo.springsecurity.model.JsonResult.ErrorData>
     * @throws
    */
    public static JsonResult<JsonResult.ErrorData> errorJson(int code, JsonResult.ErrorData error) {
        return new JsonResult<>(code, error);
    }
}
