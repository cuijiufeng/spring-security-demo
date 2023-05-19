package io.inferiority.demo.springsecurity.model.vo;

import io.inferiority.demo.springsecurity.model.PermissionEntity;
import lombok.Data;

import java.util.List;

/**
 * @author cuijiufeng
 * @Class PermissionVo
 * @Date 2023/5/19 14:33
 */
@Data
public class PermissionVo extends PermissionEntity {
    private List<PermissionVo> children;
}
