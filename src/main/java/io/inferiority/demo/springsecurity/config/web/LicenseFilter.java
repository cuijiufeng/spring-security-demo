package io.inferiority.demo.springsecurity.config.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inferiority.demo.springsecurity.dao.LicenseMapper;
import io.inferiority.demo.springsecurity.exception.ErrorEnum;
import io.inferiority.demo.springsecurity.model.LicenseEntity;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import io.inferiority.demo.springsecurity.utils.LicenseUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author cuijiufeng
 * @Class LicenseFilter
 * @Date 2023/6/9 14:48
 */
@Component
public class LicenseFilter extends OncePerRequestFilter {
    private final AntPathMatcher ANT_MATCHER = new AntPathMatcher();
    @Value("${auth.white.list:/auth/**,/license/**}")
    private String[] authWhiteList;
    @Autowired
    private LicenseMapper licenseMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //白名单
        if (Arrays.stream(authWhiteList).noneMatch(pattern -> ANT_MATCHER.match(pattern, request.getServletPath()))) {
            LicenseEntity license = licenseMapper.selectOne(Wrappers.lambdaQuery());
            if (license == null) {
                response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.getWriter().print(new ObjectMapper().writeValueAsString(JsonResultUtil.errorJson(500, ErrorEnum.ILLEGAL_LICENSE)));
                return;
            }
            try {
                LicenseUtil.parseLicense(Base64.decodeBase64(license.getLicense()));
            } catch (Exception e) {
                response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.getWriter().print(new ObjectMapper().writeValueAsString(JsonResultUtil.errorJson(500, ErrorEnum.ILLEGAL_LICENSE)));
                return;
            }
        }
        filterChain.doFilter(request , response);
    }
}
