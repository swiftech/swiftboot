package org.swiftboot.demo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.swiftboot.demo.model.AdminUserEntity;
import org.swiftboot.demo.repository.AdminUserRepository;
import org.swiftboot.util.PasswordUtils;

import java.util.Optional;

@Component
public class Init {

    @Resource
    private AdminUserRepository adminUserRepository;
    @Resource
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initData() {
        // create a new user for testing
        Optional<AdminUserEntity> optUser = adminUserRepository.findByLoginName("admin");
        if (optUser.isEmpty()) {
            AdminUserEntity newEntity = new AdminUserEntity();
            newEntity.setLoginName("admin");
            newEntity.setLoginPwd(passwordEncoder.encode("12345678"));
            adminUserRepository.save(newEntity);
        }
    }
}
