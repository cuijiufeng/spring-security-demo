package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuijiufeng
 * @Class HelpController
 * @Date 2023/4/14 15:46
 */
@RequestMapping("help")
@RestController
public class HelpController {

    @GetMapping("/hello")
    public JsonResult<Object> help() {
        return JsonResultUtil.successJson("help - hello security");
    }
}
