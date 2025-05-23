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
import org.swiftboot.demo.dto.CustomUserDetails;
import org.swiftboot.demo.dto.UserInfoDto;
import org.swiftboot.demo.model.PlatformAuthorization;
import org.swiftboot.demo.model.UserEntity;
import org.swiftboot.demo.repository.PlatformAuthorizationRepository;
import org.swiftboot.demo.repository.UserRepository;

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
        GrantedAuthority gaRole = new SimpleGrantedAuthority("ROLE_ADMIN");
        GrantedAuthority gaPermissionA = new SimpleGrantedAuthority(PERM_A);
        GrantedAuthority gaPermissionB = new SimpleGrantedAuthority(PERM_B);
        List<CustomUserDetails> users = Arrays.asList(
                new CustomUserDetails("1001", "admin", passwordEncoder.encode("123456"), List.of(new GrantedAuthority[]{gaRole, gaPermissionA, gaPermissionB})),
                new CustomUserDetails("1002", "manager", passwordEncoder.encode("123456"), List.of(new GrantedAuthority[]{gaRole, gaPermissionA})),
                new CustomUserDetails("1003", "staff", passwordEncoder.encode("123456"), List.of(new GrantedAuthority[]{gaRole, gaPermissionB}))
        );

        users.forEach(user -> {
            Optional<UserEntity> optUser = userRepository.findById(user.getId());
            if (optUser.isEmpty()) {
                UserEntity newEntity = new UserEntity();
                newEntity.setLoginName(user.getUsername());
                newEntity.setLoginPwd(user.getPassword());
                newEntity.setPermissions(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
                userRepository.save(newEntity);
            }
        });
    }

    @Override
    public UserInfoDto findById(String id) {
        Optional<UserEntity> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            return (UserInfoDto) new UserInfoDto().populateByEntity(byId.get());
        }
        return null;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Load user for : %s".formatted(username));
        UserEntity userEntity = userRepository.findByLoginName(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        List<SimpleGrantedAuthority> auhorities = Arrays.stream(userEntity.getPermissions().split(",")).map(SimpleGrantedAuthority::new).toList();
        CustomUserDetails userDetails = new CustomUserDetails(userEntity.getId(),
                userEntity.getLoginName(), userEntity.getLoginPwd(), auhorities);
        return userDetails;

//        GrantedAuthority gaRole = new SimpleGrantedAuthority("ROLE_ADMIN");
//        GrantedAuthority gaPermissionA = new SimpleGrantedAuthority(PERM_A);
//        GrantedAuthority gaPermissionB = new SimpleGrantedAuthority(PERM_B);
//        return switch (username) {
//            case "admin" ->
//                    new CustomUserDetails("1001", "admin", passwordEncoder.encode("123456"), List.of(new GrantedAuthority[]{gaRole, gaPermissionA, gaPermissionB}));
//            case "manager" ->
//                    new CustomUserDetails("1002", "manager", passwordEncoder.encode("123456"), List.of(new GrantedAuthority[]{gaRole, gaPermissionA}));
//            default ->
//                    new CustomUserDetails("1003", "staff", passwordEncoder.encode("123456"), List.of(new GrantedAuthority[]{gaRole, gaPermissionB}));
//        };
    }

    @Override
    public UserInfoDto findUserByOpenId(String openId) {
        UserEntity userEntity = userRepository.findByOpenId(openId);
        if (userEntity == null) {
            return null;
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.populateByEntity(userEntity);
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
