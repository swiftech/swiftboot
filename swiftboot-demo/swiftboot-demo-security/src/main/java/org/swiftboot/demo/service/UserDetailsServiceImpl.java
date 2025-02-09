package org.swiftboot.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author swiftech
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Load user for : " + username);
        GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_ADMIN");
        User user = new User("admin", passwordEncoder.encode("123456"), Collections.singleton(ga));
        return user;
    }
}
