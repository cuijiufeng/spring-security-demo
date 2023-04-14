package io.inferiority.demo.springsecurity.config.security;

import io.inferiority.demo.springsecurity.model.JsonResult;
import io.inferiority.demo.springsecurity.utils.JsonResultUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author cuijiufeng
 * @Class SecurityConfig
 * @Date 2023/4/14 14:30
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class SecurityConfig {
    @Value("#{'${auth.white.list:/auth/**,/help/**}'.split(',')}")
    private String[] authWhiteList;

    //@Bean
    //public WebSecurityCustomizer webSecurityCustomizer() {
    //}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                //关闭csrf防护
                .csrf().disable()
                .httpBasic(withDefaults())
                .formLogin(login -> login
                        //登录接口
                        .loginProcessingUrl("/auth/login")
                        //登录页
                        //.loginPage("/login")
                        //登录成功访问路径
                        //.defaultSuccessUrl("/")
                )
                .logout(logout -> logout
                        //登出接口
                        .logoutUrl("/auth/logout")
                )
                .authorizeHttpRequests(authorize -> authorize
                        //白名单
                        .antMatchers(authWhiteList).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        //已认证但是权限不够
                        .accessDeniedHandler((request, response, ex) ->
                                response.getWriter().print(JsonResultUtil.errorJson(500, new JsonResult.ErrorData("403", "forbidden"))))
                        //未能通过认证，也就是未登录
                        //.authenticationEntryPoint()
                )
                //禁用session
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // @formatter:on
        return http.build();
    }
}
