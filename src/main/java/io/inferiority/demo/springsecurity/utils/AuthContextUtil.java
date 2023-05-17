package io.inferiority.demo.springsecurity.utils;

import io.inferiority.demo.springsecurity.model.RoleEntity;
import io.inferiority.demo.springsecurity.model.vo.TokenVo;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * @author cuijiufeng
 * @Class RequestContextUtil
 * @Date 2023/4/27 16:43
 */
public class AuthContextUtil {

    public static String currentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static RoleEntity currentRole() {
        return Objects.requireNonNull(currentUser(), "unauthorized").getRole();
    }

    public static TokenVo currentUser() {
        try {
            return (TokenVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}
