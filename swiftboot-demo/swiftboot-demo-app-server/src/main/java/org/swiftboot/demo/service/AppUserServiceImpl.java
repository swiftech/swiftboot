package org.swiftboot.demo.service;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.swiftboot.demo.dto.AppUserDto;
import org.swiftboot.demo.model.AppUserEntity;
import org.swiftboot.demo.repository.AppUserRepository;

import java.util.Optional;

/**
 * @author swiftech 2020-02-05
 */
@Service
public class AppUserServiceImpl implements AppUserService {

    private static final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    @Resource
    private AppUserRepository appUserRepository;

    @Override
    public AppUserDto getUserInfo(String uid) {
        Optional<AppUserEntity> byUid = appUserRepository.findById(uid);
        if (byUid.isPresent()) {
            AppUserEntity appUserEntity = byUid.get();
            log.debug(appUserEntity.getLoginName());
            AppUserDto appUserDto = new AppUserDto();
            appUserDto.setId(appUserEntity.getId());
            appUserDto.setLoginName(appUserEntity.getLoginName());
            return appUserDto;
        }
        return null;
    }
}
