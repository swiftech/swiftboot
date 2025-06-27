package org.swiftboot.demo.security;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.swiftboot.demo.model.AdminUserEntity;
import org.swiftboot.demo.repository.AdminUserRepository;

import java.util.Collections;
import java.util.Optional;

/**
 * Spring Security User Detail Service.
 *
 * @author swiftech
 * @since 3.0.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AdminUserRepository adminUserRepository;

    @Override
    public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Load user for : " + username);
//        GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_ADMIN");
//        User user = new User("admin", passwordEncoder.encode("123456"), Collections.singleton(ga));
        Optional<AdminUserEntity> optAdminUser = adminUserRepository.findByLoginName(username);
        AdminUserEntity adminUserEntity = optAdminUser.orElse(null);
        if (adminUserEntity == null) {
            log.warn("User not found for : " + username);
            throw new UsernameNotFoundException("User not found for : " + username);
        }
        GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_ADMIN");
        return new AuthUser(adminUserEntity.getId(), adminUserEntity.getLoginName(), adminUserEntity.getLoginPwd(), Collections.singleton(ga));
    }
}
