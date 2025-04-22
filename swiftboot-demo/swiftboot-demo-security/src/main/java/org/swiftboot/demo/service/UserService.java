package org.swiftboot.demo.service;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.swiftboot.demo.dto.UserInfoDto;

/**
 * @since 3.0
 */
public interface UserService {

    /**
     *
     * @param id
     * @return
     */
    UserInfoDto findById(String id);

    /**
     *
     * @param openId
     * @return
     */
    UserInfoDto findUserByOpenId(String openId);

    /**
     *
     * @param openId
     * @param accessToken from OAuth2 provider
     * @param refreshToken from OAuth2 provider
     * @return
     */
    UserInfoDto createUserFromOAuth2(String openId, String nickName,
                                     OAuth2AccessToken accessToken, OAuth2RefreshToken refreshToken);
}
