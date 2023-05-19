package io.inferiority.demo.springsecurity.service;

import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.model.RoleEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;

import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/5/15 23:33
 */
public interface IRoleService {
    PageInfo<RoleEntity> list(PageDto page, RoleEntity searchRole);

    void edit(RoleEntity role, List<String> pids);

    void delete(List<String> ids);
}
