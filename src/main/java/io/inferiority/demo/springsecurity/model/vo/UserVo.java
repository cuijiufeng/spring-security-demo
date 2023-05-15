package io.inferiority.demo.springsecurity.model.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.inferiority.demo.springsecurity.model.PermissionEntity;
import io.inferiority.demo.springsecurity.model.RoleEntity;
import io.inferiority.demo.springsecurity.model.UserEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author cuijiufeng
 * @Class AuthVo
 * @Date 2023/5/9 11:21
 */
@Data
@TableName("sys_user")
public class UserVo extends UserEntity implements Serializable {
    private RoleEntity role;
    private List<PermissionEntity> permissions;
}
