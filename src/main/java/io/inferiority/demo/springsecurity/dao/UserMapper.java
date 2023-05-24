package io.inferiority.demo.springsecurity.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import io.inferiority.demo.springsecurity.config.mybatis.CacheManagerCache;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.TokenVo;
import io.inferiority.demo.springsecurity.model.vo.UserVo;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cuijiufeng
 * @Class UserMapper
 * @Date 2023/4/14 16:02
 */
@Mapper
@Repository
@CacheNamespace(implementation = CacheManagerCache.class)
public interface UserMapper extends BaseMapper<UserEntity> {

    @Select("select ${ew.sqlSelect} from sys_user ${ew.customSqlSegment}")
    TokenVo selectOneTokenVo(@Param(Constants.WRAPPER) Wrapper<UserEntity> wrapper);

    @Select("select ${ew.sqlSelect} from sys_user ${ew.customSqlSegment}")
    UserVo selectOneUserVo(@Param(Constants.WRAPPER) Wrapper<UserEntity> wrapper);

    @Select("select ${ew.sqlSelect} from sys_user ${ew.customSqlSegment}")
    @Results({@Result(column = "role_id", property = "roleId"),
            @Result(column = "role_id", property = "role", one = @One(select = "io.inferiority.demo.springsecurity.dao.RoleMapper.selectById"))})
    List<UserVo> selectListUserVo(@Param(Constants.WRAPPER) Wrapper<UserEntity> wrapper);
}
