package io.inferiority.demo.springsecurity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author cuijiufeng
 * @Class PasswordEncoderTest
 * @Date 2023/4/27 10:25
 */
@Slf4j
public class PasswordEncoderTest {
    @Test
    public void testPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        log.info(passwordEncoder.encode("admin"));
    }
}
