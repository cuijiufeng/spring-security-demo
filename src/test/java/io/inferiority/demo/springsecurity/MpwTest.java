package io.inferiority.demo.springsecurity;

import com.baomidou.mybatisplus.core.toolkit.AES;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author cuijiufeng
 * @Class MpwTest
 * @Date 2023/4/27 9:15
 */
@Slf4j
public class MpwTest {
    @Test
    public void testMpw() {
        String strKey = AES.generateRandomKey();
        log.info("--mpw.key={}", strKey);
        log.info("mpw:{}", AES.encrypt("root", strKey));
    }
}
