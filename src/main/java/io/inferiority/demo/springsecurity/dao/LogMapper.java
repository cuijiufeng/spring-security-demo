package io.inferiority.demo.springsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.inferiority.demo.springsecurity.model.LogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author cuijiufeng
 * @Class LogMapper
 * @Date 2023/4/27 17:01
 */
@Mapper
@Repository
public interface LogMapper extends BaseMapper<LogEntity> {
}
