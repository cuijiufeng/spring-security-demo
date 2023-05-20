package io.inferiority.demo.springsecurity.utils;

import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.RoleEntity;
import io.inferiority.demo.springsecurity.model.vo.UserVo;

import java.util.List;
import java.util.Objects;

/**
 * @author cuijiufeng
 * @date 2023/5/18 21:17
 */
public class PermissionCompareUtil {

    public static void compareByUsers(String superUserId, List<UserVo> users) {
        for (UserVo user : users) {
            compare(superUserId, user.getId(), user.getRole());
        }
    }

    public static void compareByRoles(String superUserId, List<RoleEntity> roles) {
        for (RoleEntity role : roles) {
            compare(superUserId, null, role);
        }
    }

    public static void compare(String superUserId, String userId, RoleEntity role) {
        if (Objects.equals(superUserId, AuthContextUtil.currentUser().getId())) {
            return;
        }
        //修改自己
        if (Objects.equals(AuthContextUtil.currentUser().getId(), userId)) {
            return;
        }
        if (AuthContextUtil.currentRole().getLevel() >= role.getLevel()) {
            throw new ServiceException(JsonResultUtil.PERMISSION_DENIED.getData());
        }
    }
}
