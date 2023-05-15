package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.dao.RoleMapper;
import io.inferiority.demo.springsecurity.model.RoleEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/5/15 23:33
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<RoleEntity> list(PageDto page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true, null, page.isAll());
        List<RoleEntity> roles = roleMapper.selectList(Wrappers.lambdaQuery());
        return new PageInfo<>(roles);
    }
}
