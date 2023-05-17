package io.inferiority.demo.springsecurity.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.inferiority.demo.springsecurity.model.vo.TokenVo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Objects;

/**
 * @author cuijiufeng
 * @Class RequestContextUtil
 * @Date 2023/4/27 16:43
 */
public class RequestContextUtil {

    public static String currentUsername(Key jwtPubKey) {
        TokenVo user = currentUser(jwtPubKey);
        return Objects.isNull(user) ? null : user.getUsername();
    }

    public static TokenVo currentUser(Key jwtPubKey) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String tokenHeader = request.getHeader(JwtUtil.TOKEN_HEADER);
        if (Objects.isNull(tokenHeader)) {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            Objects.requireNonNull(response, "response can't be null");
            tokenHeader = response.getHeader(JwtUtil.TOKEN_HEADER);
        }
        Objects.requireNonNull(tokenHeader, "not Logged In");
        Object user = JwtUtil.parseJwt(jwtPubKey, tokenHeader);
        return new ObjectMapper().convertValue(user, TokenVo.class);
    }
}
