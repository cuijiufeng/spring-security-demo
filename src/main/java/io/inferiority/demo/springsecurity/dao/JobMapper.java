package io.inferiority.demo.springsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.inferiority.demo.springsecurity.model.JobEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author cuijiufeng
 * @Class JobMapper
 * @Date 2023/5/22 11:02
 */
@Mapper
@Repository
public interface JobMapper extends BaseMapper<JobEntity> {
}
