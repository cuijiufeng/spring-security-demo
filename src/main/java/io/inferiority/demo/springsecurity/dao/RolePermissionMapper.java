package io.inferiority.demo.springsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.inferiority.demo.springsecurity.model.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author cuijiufeng
 * @date 2023/4/15 17:58
 */
@Mapper
@Repository
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
}
