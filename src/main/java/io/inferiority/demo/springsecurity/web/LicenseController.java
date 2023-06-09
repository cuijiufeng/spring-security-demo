package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.aop.log.Log;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.service.ILicenseService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

/**
 * @author cuijiufeng
 * @Class LicenseController
 * @Date 2023/6/9 15:59
 */
@RequestMapping("license")
@RestController
public class LicenseController extends BaseController {
    @Autowired
    private ILicenseService licenseService;

    @Log("license info")
    @GetMapping("info")
    public JsonResult<?> info() throws ParseException, GeneralSecurityException, IOException {
        return JsonResultUtil.successJson(licenseService.info());
    }

    @Log("license update")
    @PostMapping("update")
    public JsonResult<Void> update(MultipartFile file) {
        licenseService.update(readMultipartFile(file));
        return JsonResultUtil.successJson();
    }
}
