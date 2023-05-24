package io.inferiority.demo.springsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.inferiority.demo.springsecurity.config.mybatis.CacheManagerCache;
import io.inferiority.demo.springsecurity.model.RoleEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author cuijiufeng
 * @date 2023/4/15 17:47
 */
@Mapper
@Repository
@CacheNamespace(implementation = CacheManagerCache.class)
public interface RoleMapper extends BaseMapper<RoleEntity> {
}
