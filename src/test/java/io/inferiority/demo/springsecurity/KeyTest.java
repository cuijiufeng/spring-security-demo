package io.inferiority.demo.springsecurity;


import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.net.URL;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyTest {

    @Test
    public void testKey() throws Exception {
        //生成密钥对
        // openssl genrsa -out rsa.pem 1024
        //分离出公钥且pem转der
        // openssl rsa -in rsa.pem -out rsa.pub.der -outform DER -pubout
        //pkcs1转pkcs8且pem转der
        // openssl pkcs8 -topk8 -in rsa.pem -out rsa.der -inform PEM -outform DER -nocrypt
        URL privFile = ResourceUtils.getURL("classpath:rsa.der");
        URL pubFile = ResourceUtils.getURL("classpath:rsa.pub.der");

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        System.out.println(keyFactory.generatePublic(new X509EncodedKeySpec(IOUtils.toByteArray(pubFile))));
        System.out.println(keyFactory.generatePrivate(new PKCS8EncodedKeySpec(IOUtils.toByteArray(privFile))));
    }
}