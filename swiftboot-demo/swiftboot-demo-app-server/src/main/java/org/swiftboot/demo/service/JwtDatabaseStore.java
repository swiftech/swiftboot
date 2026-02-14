package org.swiftboot.demo.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.service.JwtStore;
import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.demo.model.AppUserEntity;
import org.swiftboot.demo.repository.AppUserRepository;

import java.util.Optional;

/**
 * Stores JWT in database.
 *
 * @since 3.1
 */
public class JwtDatabaseStore implements JwtStore {

    private static final Logger log = LoggerFactory.getLogger(JwtDatabaseStore.class);

    @Resource
    private AppUserRepository userRepository;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void save(JwtAuthentication jwtAuthentication) {
        String userId = jwtTokenProvider.getUserId(jwtAuthentication.getAccessToken().getTokenValue());
        AppUserEntity appUserEntity = safeGetUser(userId);
        appUserEntity.setAccessToken(jwtAuthentication.getAccessToken().getTokenValue());
        appUserEntity.setAccessTokenExpiresAt(jwtAuthentication.getAccessToken().getExpiresAt());
        appUserEntity.setRefreshToken(jwtAuthentication.getRefreshToken().getTokenValue());
        appUserEntity.setRefreshTokenExpiresAt(jwtAuthentication.getRefreshToken().getExpiresAt());
        userRepository.save(appUserEntity);
    }

    @Override
    public void removeAccessToken(String accessToken) {
        String userId;
        try {
            userId = jwtTokenProvider.getUserId(accessToken);
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
        }
        AppUserEntity appUserEntity = safeGetUser(userId);
        appUserEntity.setAccessToken(null);
        appUserEntity.setAccessTokenExpiresAt(null);
        userRepository.save(appUserEntity);
    }

    @Override
    public void removeRefreshToken(String refreshToken) {
        String userId;
        try {
            userId = jwtTokenProvider.getUserId(refreshToken);
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
        }
        AppUserEntity appUserEntity = safeGetUser(userId);
        appUserEntity.setRefreshToken(null);
        appUserEntity.setRefreshTokenExpiresAt(null);
        userRepository.save(appUserEntity);
    }

    @Override
    public JwtAuthentication loadByAccessToken(String accessToken) {
        String userId = jwtTokenProvider.getUserId(accessToken);
        AppUserEntity aue = safeGetUser(userId);
        return new JwtAuthentication(aue.getAccessToken(), aue.getAccessTokenExpiresAt(), aue.getRefreshToken(), aue.getRefreshTokenExpiresAt());
    }

    @Override
    public JwtAuthentication loadByRefreshToken(String refreshToken) {
        String userId = jwtTokenProvider.getUserId(refreshToken);
        AppUserEntity aue = safeGetUser(userId);
        return new JwtAuthentication(aue.getAccessToken(), aue.getAccessTokenExpiresAt(), aue.getRefreshToken(), aue.getRefreshTokenExpiresAt());
    }

    private AppUserEntity safeGetUser(String userId) {
        Optional<AppUserEntity> optUserEntity = userRepository.findById(userId);
        if (optUserEntity.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return optUserEntity.get();
    }

}
