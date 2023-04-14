package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuijiufeng
 * @Class OtherController
 * @Date 2023/4/14 16:30
 */
@RestController
public class OtherController {
    @GetMapping("/")
    public JsonResult<Object> index() {
        return JsonResultUtil.successJson("hello security");
    }
}
