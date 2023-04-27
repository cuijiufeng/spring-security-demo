package io.inferiority.demo.springsecurity.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.inferiority.demo.springsecurity.model.vo.UserVo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Objects;

/**
 * @author cuijiufeng
 * @Class RequestContextUtil
 * @Date 2023/4/27 16:43
 */
public class RequestContextUtil {

    public static UserVo getCurrentUser(Key jwtPubKey) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String tokenHeader = request.getHeader(JwtUtil.TOKEN_HEADER);
        Objects.requireNonNull(tokenHeader, "token can't be null");
        Object user = JwtUtil.parseJwt(jwtPubKey, tokenHeader);
        return new ObjectMapper().convertValue(user, UserVo.class);
    }
}
