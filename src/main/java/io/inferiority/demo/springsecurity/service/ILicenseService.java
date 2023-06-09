package io.inferiority.demo.springsecurity.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

/**
 * @author cuijiufeng
 * @Class ILicenseService
 * @Date 2023/6/9 16:01
 */
public interface ILicenseService {
    Object info() throws ParseException, GeneralSecurityException, IOException;

    void update(byte[] license);
}
