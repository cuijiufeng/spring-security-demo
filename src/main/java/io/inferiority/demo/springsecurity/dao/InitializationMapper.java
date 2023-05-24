package io.inferiority.demo.springsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.inferiority.demo.springsecurity.config.mybatis.CacheManagerCache;
import io.inferiority.demo.springsecurity.model.InitializationEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author cuijiufeng
 * @Class InitializationMapper
 * @Date 2023/5/19 8:59
 */
@Mapper
@Repository
@CacheNamespace(implementation = CacheManagerCache.class)
public interface InitializationMapper extends BaseMapper<InitializationEntity> {
}
