package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.dao.RoleMapper;
import io.inferiority.demo.springsecurity.model.RoleEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.service.IRoleService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/5/15 23:33
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Value("${default.role.prefix:ROLE_}")
    private String defaultRolePrefix;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<RoleEntity> list(PageDto page, RoleEntity searchRole) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), true, null, page.isAll());
        List<RoleEntity> roles = roleMapper.selectList(Wrappers.<RoleEntity>lambdaQuery()
                .likeRight(StringUtils.isNotBlank(searchRole.getRoleName()), RoleEntity::getRoleName, searchRole.getRoleName())
                .eq(StringUtils.isNotBlank(searchRole.getRoleKey()), RoleEntity::getRoleKey, defaultRolePrefix + searchRole.getRoleKey())
                .between(ArrayUtils.isNotEmpty(page.getDateRange()), RoleEntity::getCreateTime, page.getStart(), page.getEnd())
                .orderByAsc(RoleEntity::getCreateTime));
        PageInfo<RoleEntity> pageInfo = new PageInfo<>(roles);
        pageInfo.getList().forEach(role -> role.setRoleKey(role.getRoleKey().replace(defaultRolePrefix, "")));
        return pageInfo;
    }
}
