package org.swiftboot.demo.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.swiftboot.demo.dto.AppUserDto;
import org.swiftboot.demo.model.AppUserEntity;
import org.swiftboot.demo.repository.AppUserRepository;
import org.swiftboot.util.PasswordUtils;

import java.util.Optional;

/**
 * @author swiftech 2020-02-05
 */
@Service
public class AppUserServiceImpl implements AppUserService {

    private static final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    @Resource
    private AppUserRepository appUserRepository;

    /**
     * Init default user for testing if it does not exist.
     */
    @PostConstruct
    public void initData() {
        // create a new user for testing
        Optional<AppUserEntity> optUser = appUserRepository.findByLoginName("13866669999");
        if (optUser.isEmpty()) {
            AppUserEntity newEntity = new AppUserEntity();
            newEntity.setLoginName("13866669999");
            newEntity.setLoginPwd(PasswordUtils.createPassword("12345678"));
            appUserRepository.save(newEntity);
        }
    }

    @Override
    public AppUserDto getUserInfo(String uid) {
        Optional<AppUserEntity> byUid = appUserRepository.findById(uid);
        if (byUid.isPresent()) {
            AppUserEntity appUserEntity = byUid.get();
            log.debug(appUserEntity.getLoginName());
            AppUserDto appUserDto = new AppUserDto();
            appUserDto.setId(appUserEntity.getId());
            appUserDto.setLoginName(appUserEntity.getLoginName());
            appUserDto.setLastLoginTime(appUserEntity.getLastLoginTime());
            appUserDto.setCreateTime(appUserEntity.getCreateTime());
            return appUserDto;
        }
        return null;
    }
}
