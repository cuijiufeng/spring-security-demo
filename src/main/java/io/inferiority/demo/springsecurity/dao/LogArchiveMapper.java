package io.inferiority.demo.springsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.inferiority.demo.springsecurity.model.LogArchiveEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author cuijiufeng
 * @date 2023/5/21 15:41
 */
@Mapper
@Repository
public interface LogArchiveMapper extends BaseMapper<LogArchiveEntity> {
}
