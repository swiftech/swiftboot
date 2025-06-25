package org.swiftboot.demo.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;
import org.swiftboot.demo.constants.RoleConstants;
import org.swiftboot.demo.dto.CustomUserDetails;
import org.swiftboot.demo.dto.UserInfoDto;
import org.swiftboot.demo.model.PlatformAuthorization;
import org.swiftboot.demo.model.UserEntity;
import org.swiftboot.demo.repository.PlatformAuthorizationRepository;
import org.swiftboot.demo.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.swiftboot.demo.constants.PermissionConstants.PERM_A;
import static org.swiftboot.demo.constants.PermissionConstants.PERM_B;

/**
 * @author swiftech
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRepository userRepository;

    @Resource
    private PlatformAuthorizationRepository platformAuthorizationRepository;

    @PostConstruct
    public void initData() {
        // create a new user for testing
        Optional<UserEntity> optAdminUser = userRepository.findByLoginName("admin");
        if (optAdminUser.isEmpty()) {
            log.info("测试用户数据不存在，初始化数据");
            GrantedAuthority gaRole = new SimpleGrantedAuthority(RoleConstants.ROLE_ADMIN);
            GrantedAuthority gaPermissionA = new SimpleGrantedAuthority(PERM_A);
            GrantedAuthority gaPermissionB = new SimpleGrantedAuthority(PERM_B);
            List<CustomUserDetails> users = Arrays.asList(
                    new CustomUserDetails("1001", "admin", passwordEncoder.encode("123456"), RoleConstants.ROLE_ADMIN, List.of(new GrantedAuthority[]{gaPermissionA, gaPermissionB})),
                    new CustomUserDetails("1002", "manager", passwordEncoder.encode("123456"), RoleConstants.ROLE_MANAGER, List.of(new GrantedAuthority[]{gaPermissionA})),
                    new CustomUserDetails("1003", "staff", passwordEncoder.encode("123456"), RoleConstants.ROLE_STAFF, List.of(new GrantedAuthority[]{gaPermissionB}))
            );

            users.forEach(user -> {
                Optional<UserEntity> optUser = userRepository.findById(user.getId());
                if (optUser.isEmpty()) {
                    UserEntity newEntity = new UserEntity();
                    newEntity.setLoginName(user.getUsername());
                    newEntity.setLoginPwd(user.getPassword());
                    newEntity.setRole(user.getRole());
                    newEntity.setPermissions(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
                    userRepository.save(newEntity);
                }
            });
        }
    }

    @Override
    public UserInfoDto findById(String id) {
        Optional<UserEntity> byId = userRepository.findById(id);
        return byId.map(userEntity -> (UserInfoDto) new UserInfoDto().populateByEntity(userEntity)).orElse(null);
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Load user for : %s".formatted(username));
        Optional<UserEntity> optUser = userRepository.findByLoginName(username);
        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        UserEntity userEntity = optUser.get();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(Arrays.stream(userEntity.getPermissions().split(",")).map(SimpleGrantedAuthority::new).toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole()));
        CustomUserDetails userDetails = new CustomUserDetails(userEntity.getId(),
                userEntity.getLoginName(), userEntity.getLoginPwd(), userEntity.getRole(), authorities);
        return userDetails;
    }

    @Override
    public UserInfoDto findUserByOpenId(String openId) {
        Optional<UserEntity> optUser = userRepository.findByOpenId(openId);
        if (optUser.isEmpty()) {
            return null;
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.populateByEntity(optUser.get());
        return userInfoDto;
    }

    @Override
    public UserInfoDto createUserFromOAuth2(String openId, String nickName, OAuth2AccessToken accessToken, OAuth2RefreshToken refreshToken) {
        UserEntity userEntity = new UserEntity();
        userEntity.setOpenId(openId);
        userEntity.setLoginName("oauth_user_" + RandomStringUtils.secure().nextAlphabetic(6));
        userEntity.setLoginPwd(passwordEncoder.encode(RandomStringUtils.secure().nextAlphabetic(16)));
        userEntity.setNickName(nickName);
        userEntity = userRepository.save(userEntity);
        PlatformAuthorization pa = platformAuthorizationRepository.findByOpenId(openId);
        if (pa == null) {
            pa = new PlatformAuthorization();
        }
        pa.setUserId(userEntity.getId());
        pa.setOpenId(openId);
        pa.setUserName(nickName);
        pa.setAccessToken(accessToken.getTokenValue());
        if (accessToken.getExpiresAt() != null) {
            pa.setExpiresAt(accessToken.getExpiresAt().toEpochMilli());
        }
        if (refreshToken != null) {
            pa.setRefreshToken(refreshToken.getTokenValue());
            if (refreshToken.getExpiresAt() != null) {
                pa.setRefreshTokenExpiresAt(refreshToken.getExpiresAt().toEpochMilli());
            }
        }
        platformAuthorizationRepository.save(pa);
        return (UserInfoDto) new UserInfoDto().populateByEntity(userEntity);
    }
}
