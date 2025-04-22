package org.swiftboot.demo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.swiftboot.demo.model.AppUserEntity;
import org.swiftboot.demo.repository.AppUserRepository;
import org.swiftboot.util.PasswordUtils;

import java.util.Optional;

@Component
public class Init {

    @Resource
    private AppUserRepository appUserRepository;

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
}
