package io.inferiority.demo.springsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.inferiority.demo.springsecurity.config.mybatis.CacheManagerCache;
import io.inferiority.demo.springsecurity.model.LicenseEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author cuijiufeng
 * @Class LicenseMapper
 * @Date 2023/6/9 14:54
 */
@Mapper
@Repository
@CacheNamespace(implementation = CacheManagerCache.class)
public interface LicenseMapper extends BaseMapper<LicenseEntity> {
}
