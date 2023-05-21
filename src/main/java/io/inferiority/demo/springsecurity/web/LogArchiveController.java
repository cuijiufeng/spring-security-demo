package io.inferiority.demo.springsecurity.web;

import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.aop.log.Log;
import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.model.LogArchiveEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.ILogArchiveService;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cuijiufeng
 * @date 2023/5/21 16:50
 */
@RequestMapping("log-archive")
@RestController
public class LogArchiveController {
    @Autowired
    private ILogArchiveService logArchiveService;

    @Log("log archive list")
    @PreAuthorize("hasAnyAuthority('log:archive:select')")
    @GetMapping("list")
    public JsonResult<PageInfo<LogArchiveEntity>> list(@Validated PageDto page) {
        return JsonResultUtil.successJson(logArchiveService.list(page));
    }

    @Log("download log archive")
    @PreAuthorize("hasAnyAuthority('log:archive:download')")
    @GetMapping("download")
    public ResponseEntity<byte[]> download(@RequestParam String id) {
        LogArchiveEntity download = logArchiveService.download(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, download.getFileName())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(download.getLogFile());
    }
}
