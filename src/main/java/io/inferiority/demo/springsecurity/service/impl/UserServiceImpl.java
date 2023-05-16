package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.dao.UserMapper;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.model.vo.UserVo;
import io.inferiority.demo.springsecurity.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/5/15 19:19
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<UserVo> list(PageDto page, UserEntity searchUser) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true, null, page.isAll());
        List<UserVo> users = userMapper.selectListUserVo(Wrappers.<UserEntity>lambdaQuery()
                .select(UserEntity.class, c -> true)
                .eq(StringUtils.isNotBlank(searchUser.getRoleId()), UserEntity::getRoleId, searchUser.getRoleId())
                .likeRight(StringUtils.isNotBlank(searchUser.getUsername()), UserEntity::getUsername, searchUser.getUsername()));
        return new PageInfo<>(users);
    }
}
