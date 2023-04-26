package io.inferiority.demo.springsecurity.utils;

import io.inferiority.demo.springsecurity.exception.BaseErrorEnum;
import io.inferiority.demo.springsecurity.model.JsonResult;

/**
 * @ClassName: JsonResultUtils
 * @Date: 2021/1/6 13:05
 * @author: cjf
 */
public class JsonResultUtil {
    public static final JsonResult<BaseErrorEnum> UNKNOWN = JsonResultUtil.errorJson(500, BaseErrorEnum.BUILD.apply("-1", "unknown"));
    public static final JsonResult<BaseErrorEnum> UNAUTHORIZED = JsonResultUtil.errorJson(401, BaseErrorEnum.BUILD.apply("10401", "unauthorized"));
    public static final JsonResult<BaseErrorEnum> FORBIDDEN = JsonResultUtil.errorJson(403, BaseErrorEnum.BUILD.apply("10403", "forbidden"));

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
    public static JsonResult<BaseErrorEnum> errorJson(int code, BaseErrorEnum error) {
        return new JsonResult<>(code, error);
    }
}
