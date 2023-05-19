package io.inferiority.demo.springsecurity.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import io.inferiority.demo.springsecurity.model.PermissionEntity;
import io.inferiority.demo.springsecurity.model.vo.PermissionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/4/15 17:58
 */
@Mapper
@Repository
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

    @Select("select ${ew.sqlSelect} from sys_permission ${ew.customSqlSegment}")
    List<PermissionVo> selectTree(@Param(Constants.WRAPPER) Wrapper<PermissionEntity> wrapper);
}
