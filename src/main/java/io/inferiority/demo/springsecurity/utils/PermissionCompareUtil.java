package io.inferiority.demo.springsecurity.utils;

import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.RoleEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author cuijiufeng
 * @date 2023/5/18 21:17
 */
public class PermissionCompareUtil {

    public static void compare(String superUserId, List<RoleEntity> roles) {
        if (Objects.equals(superUserId, AuthContextUtil.currentUser().getId())) {
            return;
        }
        if (roles.stream().anyMatch(role -> AuthContextUtil.currentRole().getLevel() >= role.getLevel())) {
            throw new ServiceException(JsonResultUtil.PERMISSION_DENIED.getData());
        }
    }

    public static void compare(String superUserId, RoleEntity role) {
        compare(superUserId, Collections.singletonList(role));
    }
}
