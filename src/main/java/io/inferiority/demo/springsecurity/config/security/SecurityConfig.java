package io.inferiority.demo.springsecurity.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.inferiority.demo.springsecurity.config.AuthenticationTokenFilter;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;

/**
 * @author cuijiufeng
 * @Class SecurityConfig
 * @Date 2023/4/14 14:30
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    public static final AuthenticationEntryPoint AUTHENTICATION_ENTRY_POINT = (request, response, ex) -> {
        log.info(ex.getMessage());
        response.getWriter().print(new ObjectMapper().writeValueAsString(JsonResultUtil.UNAUTHORIZED));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    };
    public static final AccessDeniedHandler ACCESS_DENIED_HANDLER = (request, response, ex) -> {
        log.info(ex.getMessage());
        response.getWriter().print(new ObjectMapper().writeValueAsString(JsonResultUtil.FORBIDDEN));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    };

    @Value("${token.header:Authorization}")
    private String tokenHeader;
    @Value("#{T(org.springframework.boot.convert.DurationStyle).detectAndParse('${token.duration:5m}')}")
    private Duration tokenDuration;
    @Value("#{T(io.inferiority.demo.springsecurity.utils.RsaKeyUtil).parsePublicKey('${jwt.pub.key:classpath:rsa.pub.der}')}")
    private PublicKey jwtPubKey;
    @Value("#{T(io.inferiority.demo.springsecurity.utils.RsaKeyUtil).parsePrivateKey('${jwt.priv.key:classpath:rsa.der}')}")
    private PrivateKey jwtPrivKey;
    @Value("#{'${auth.white.list:/auth/**}'.split(',')}")
    private String[] authWhiteList;
    @Autowired
    private UserDetailsService userDetailsService;

    //@Bean
    //public WebSecurityCustomizer webSecurityCustomizer() {
    //}

    /**
     * 全局认证管理器
     * @param http
     * @return org.springframework.security.authentication.AuthenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                //关闭csrf防护
                .csrf().disable()
                //允许跨域
                .cors().and()
                //.httpBasic(withDefaults())
                //禁用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //添加token验证过滤器
                .addFilterBefore(new AuthenticationTokenFilter(tokenHeader, tokenDuration, jwtPubKey, jwtPrivKey, authWhiteList), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
                //白名单
                .antMatchers(authWhiteList).permitAll()
                //除了上面的请求以外所有的请求全部需要认证
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                //已认证但是权限不够
                .accessDeniedHandler(ACCESS_DENIED_HANDLER)
                //未能通过认证，也就是未登录
                .authenticationEntryPoint(AUTHENTICATION_ENTRY_POINT);
        // @formatter:on
        return http.build();
    }
}
