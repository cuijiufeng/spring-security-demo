package io.inferiority.demo.springsecurity.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author cuijiufeng
 * @date 2023/4/16 0:09
 */
public class CryptoUtil {

    public static PrivateKey parsePrivateKey(String urlPath) throws IOException, GeneralSecurityException {
        URL url = ResourceUtils.getURL(urlPath);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(IOUtils.toByteArray(url)));
    }

    public static PublicKey parsePublicKey(String urlPath) throws IOException, GeneralSecurityException {
        URL url = ResourceUtils.getURL(urlPath);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(IOUtils.toByteArray(url)));
    }
}
