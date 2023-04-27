package io.inferiority.demo.springsecurity.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author cuijiufeng
 * @Class UserMapper
 * @Date 2023/4/14 16:02
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<UserEntity> {

    @Select("select ${ew.sqlSelect} from sys_user ${ew.customSqlSegment}")
    UserVo selectOneVo(@Param(Constants.WRAPPER) Wrapper<UserEntity> wrapper);
}
