package io.inferiority.demo.springsecurity.web;

import io.inferiority.demo.springsecurity.exception.ErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 * @Class: BaseController
 * @Date: 2021/9/17 17:47
 * @auth: cuijiufeng
 */
@Slf4j
@Component
public class BaseController {

    /*
     * 读取上传文件
     * @param file
     * @return byte[]
     */
    public byte[] readMultipartFile(MultipartFile file) {
        Objects.requireNonNull(file, "multipart file can not be null");
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new ServiceException(ErrorEnum.UPLOAD_FILE_ERROR, e);
        }
    }
}
