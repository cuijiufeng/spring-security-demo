package io.inferiority.demo.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.inferiority.demo.springsecurity.dao.UserMapper;
import io.inferiority.demo.springsecurity.exception.ErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.UserEntity;
import io.inferiority.demo.springsecurity.model.vo.PageDto;
import io.inferiority.demo.springsecurity.model.vo.UserVo;
import io.inferiority.demo.springsecurity.service.IUserService;
import io.inferiority.demo.springsecurity.utils.RequestContextUtil;
import io.inferiority.demo.springsecurity.utils.SnowflakeId;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.security.PublicKey;
import java.util.Date;
import java.util.List;

/**
 * @author cuijiufeng
 * @date 2023/5/15 19:19
 */
@Service
public class UserServiceImpl implements IUserService {
    @Value("#{T(io.inferiority.demo.springsecurity.utils.CryptoUtil).parsePublicKey('${jwt.pub.key:classpath:jwt/rsa.pub.der}')}")
    private PublicKey jwtPubKey;
    @Autowired
    private PasswordEncoder passwordEncoder;
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

    @Override
    @Transactional
    public void edit(UserEntity user) {
        Date currentTime = new Date();
        //判断权限是否足够
        if (StringUtils.isNotBlank(user.getRoleId())
                && !RequestContextUtil.currentUser(jwtPubKey).getRoleId().equals(user.getRoleId())) {
            //TODO 2023/5/17 17:53
        }
        //编辑
        if (StringUtils.isNotBlank(user.getId())) {
            return;
        }
        //新增
        List<UserEntity> existUser = userMapper.selectList(Wrappers.<UserEntity>lambdaQuery()
                .eq(UserEntity::getUsername, user.getUsername()));
        if (!CollectionUtils.isEmpty(existUser)) {
            throw new ServiceException(ErrorEnum.EXIST_USER_FAILED);
        }
        user.setId(SnowflakeId.generateStrId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(currentTime);
        user.setLastUpdatePasswordTime(currentTime);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        if (userMapper.insert(user) != 1) {
            throw new ServiceException(ErrorEnum.ADD_EDIT_USER_FAILED);
        }
    }
}
