package io.inferiority.demo.springsecurity.service;

import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.model.RoleEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;

/**
 * @author cuijiufeng
 * @date 2023/5/15 23:33
 */
public interface IRoleService {
    PageInfo<RoleEntity> list(PageDto page, RoleEntity searchRole);
}
