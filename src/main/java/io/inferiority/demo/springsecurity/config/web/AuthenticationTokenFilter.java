package io.inferiority.demo.springsecurity.config.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.inferiority.demo.springsecurity.model.vo.UserVo;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import io.inferiority.demo.springsecurity.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Arrays;

/**
 * @author cuijiufeng
 * @date 2023/4/15 0:03
 */
@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private final AntPathMatcher ANT_MATCHER = new AntPathMatcher();
    private final String tokenHeader;
    private final Duration tokenDuration;
    private final PublicKey jwtPubKey;
    private final PrivateKey jwtPrivKey;
    private final String[] authWhiteList;

    public AuthenticationTokenFilter(String tokenHeader, Duration tokenDuration, PublicKey jwtPubKey, PrivateKey jwtPrivKey, String[] authWhiteList) {
        this.tokenHeader = tokenHeader;
        this.tokenDuration = tokenDuration;
        this.jwtPubKey = jwtPubKey;
        this.jwtPrivKey = jwtPrivKey;
        this.authWhiteList = authWhiteList;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //白名单
        if (Arrays.stream(authWhiteList).anyMatch(pattern -> ANT_MATCHER.match(pattern, request.getServletPath()))) {
            filterChain.doFilter(request , response);
            return;
        }
        // 1、从请求头中获取token，如果请求头中不存在token
        String token = request.getHeader(tokenHeader);
        try {
            // 2、对token进行解析
            Object user = JwtUtil.parseJwt(jwtPubKey, token);
            UserVo userVo = new ObjectMapper().convertValue(user, UserVo.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, userVo.getAuthorities()) ;
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 5、放行
            filterChain.doFilter(request , response);

            //刷新token
            token = JwtUtil.createJwt(jwtPrivKey, user, tokenDuration.toMillis());
            log.debug("refresh token: {}", token);
            response.setHeader(tokenHeader, token);
        } catch (ExpiredJwtException e) {
            log.warn("--------------------> jwt is expire: {} - {}", request.getServletPath(), e.getMessage());
            response.getWriter().print(new ObjectMapper().writeValueAsString(JsonResultUtil.UNAUTHORIZED));
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        } catch (NullPointerException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException e) {
            log.warn("--------------------> invalid token: {} - {}", request.getServletPath(), e.getMessage());
            response.getWriter().print(new ObjectMapper().writeValueAsString(JsonResultUtil.UNAUTHORIZED));
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        } catch (Exception e) {
            log.error("--------------------> exception: {}", request.getServletPath(), e);
            response.getWriter().print(new ObjectMapper().writeValueAsString(JsonResultUtil.UNKNOWN));
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }
    }
}
