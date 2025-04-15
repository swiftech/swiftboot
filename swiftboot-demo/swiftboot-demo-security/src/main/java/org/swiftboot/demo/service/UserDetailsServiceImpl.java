package org.swiftboot.demo.service;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.swiftboot.demo.constants.PermissionConstants.PERM_A;
import static org.swiftboot.demo.constants.PermissionConstants.PERM_B;

/**
 * @author swiftech
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Load user for : %s".formatted(username));
        GrantedAuthority gaRole = new SimpleGrantedAuthority("ROLE_ADMIN");
        GrantedAuthority gaPermissionA = new SimpleGrantedAuthority(PERM_A);
        GrantedAuthority gaPermissionB = new SimpleGrantedAuthority(PERM_B);
        return switch (username) {
            case "admin" ->
                    new User("admin", passwordEncoder.encode("123456"), List.of(new GrantedAuthority[]{gaRole, gaPermissionA, gaPermissionB}));
            case "manager" ->
                    new User("manager", passwordEncoder.encode("123456"), List.of(new GrantedAuthority[]{gaRole, gaPermissionA}));
            default ->
                    new User("staff", passwordEncoder.encode("123456"), List.of(new GrantedAuthority[]{gaRole, gaPermissionB}));
        };
    }
}
