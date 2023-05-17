package io.inferiority.demo.springsecurity;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author cuijiufeng
 * @Class PasswordEncoderTest
 * @Date 2023/4/27 10:25
 */
@Slf4j
public class PasswordEncoderTest {
    @Test
    public void testPasswordEncoder() throws NoSuchAlgorithmException {
        String username = "admin";
        String password = "admin";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        MessageDigest digest = MessageDigest.getInstance("sha-256");
        digest.update(username.getBytes());
        String s = Hex.encodeHexString(digest.digest(password.getBytes()));
        log.info(passwordEncoder.encode(s));
    }
}
