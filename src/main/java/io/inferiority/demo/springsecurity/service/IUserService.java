package io.inferiority.demo.springsecurity.service;

import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.model.vo.UserVo;

/**
 * @author cuijiufeng
 * @date 2023/5/15 19:18
 */
public interface IUserService {
    PageInfo<UserVo> list(PageDto page, UserEntity searchUser);
}
