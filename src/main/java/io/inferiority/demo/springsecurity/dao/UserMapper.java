package io.inferiority.demo.springsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.inferiority.demo.springsecurity.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author cuijiufeng
 * @Class UserMapper
 * @Date 2023/4/14 16:02
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
}
