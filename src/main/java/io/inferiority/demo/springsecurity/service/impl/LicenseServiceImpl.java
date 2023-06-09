package io.inferiority.demo.springsecurity.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.inferiority.demo.springsecurity.dao.LicenseMapper;
import io.inferiority.demo.springsecurity.exception.ErrorEnum;
import io.inferiority.demo.springsecurity.exception.ServiceException;
import io.inferiority.demo.springsecurity.model.LicenseEntity;
import io.inferiority.demo.springsecurity.service.ILicenseService;
import io.inferiority.demo.springsecurity.utils.LicenseUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cuijiufeng
 * @Class LicenseServiceImpl
 * @Date 2023/6/9 16:02
 */
@Service
public class LicenseServiceImpl implements ILicenseService {
    @Autowired
    private SnowflakeGenerator snowflakeGenerator;
    @Autowired
    private LicenseMapper licenseMapper;

    @Override
    public Object info() throws ParseException, GeneralSecurityException, IOException {
        LicenseEntity licenseEntity = licenseMapper.selectOne(Wrappers.lambdaQuery());
        if (licenseEntity != null) {
            LicenseUtil.ToBeSigned toBeSigned = LicenseUtil.parseLicense(Base64.decodeBase64(licenseEntity.getLicense()));
            Map<String, String> rs = new HashMap<>();
            rs.put("name", toBeSigned.getName());
            rs.put("ip", toBeSigned.getIP());
            rs.put("mac", toBeSigned.getMAC());
            rs.put("validityPeriod", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(toBeSigned.getValidityPeriod()));
            return rs;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(byte[] license) {
        try {
            LicenseUtil.parseLicense(license);
        } catch (Exception e) {
            throw new ServiceException(ErrorEnum.ILLEGAL_LICENSE, e);
        }
        LicenseEntity licenseEntity = licenseMapper.selectOne(Wrappers.lambdaQuery());
        if (licenseEntity != null) {
            licenseEntity.setLicense(Base64.encodeBase64String(license));
            licenseMapper.updateById(licenseEntity);
            return;
        }
        licenseEntity = new LicenseEntity(snowflakeGenerator.next().toString(), Base64.encodeBase64String(license));
        licenseMapper.insert(licenseEntity);
    }
}
