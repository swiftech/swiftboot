package org.swiftboot.common.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.swiftboot.common.auth.config.JwtConfigBean;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.RefreshToken;
import org.swiftboot.util.IdUtils;

/**
 *
 */
public class JwtTokenProviderTest {

    private static final String SECRET = "1888dfcab0d44dc2eeb0311e9dc79f05f14ed90a508e5c53003a0f3f568c4430";

    private static JwtTokenProvider jwtTokenProvider;

    private static JwtConfigBean jwtConfigBean;

    @BeforeAll
    static void beforeAll() {
        jwtConfigBean = new JwtConfigBean();
        jwtConfigBean.setSecret(SECRET);
        jwtTokenProvider = new JwtTokenProvider(jwtConfigBean);
    }

    @Test
    public void testValidateToken() {
        jwtConfigBean.setAccessTokenExpirationSeconds(2);
        AccessToken accessToken = jwtTokenProvider.generateAccessToken( "swiftboot");
        Assertions.assertTrue(jwtTokenProvider.validateToken(accessToken.getTokenValue()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(Exception.class, () -> jwtTokenProvider.validateToken(accessToken.getTokenValue()));
    }

    @Test
    public void testGetUsername() {
        String uid = IdUtils.makeID("uid");
        AccessToken accessToken = jwtTokenProvider.generateAccessToken(uid, "swiftboot");
        System.out.println(accessToken.getTokenValue());
        System.out.println(accessToken.getExpiresAt());
        Assertions.assertEquals(uid, jwtTokenProvider.getUserId(accessToken.getTokenValue()));
        Assertions.assertEquals("swiftboot", jwtTokenProvider.getAddition(accessToken.getTokenValue(), JwtTokenProvider.USERNAME_KEY));
        Assertions.assertEquals("swiftboot", jwtTokenProvider.getUsername(accessToken.getTokenValue()));
    }

    @Test
    public void testRefreshToken() {
        jwtConfigBean.setRefreshTokenExpirationSeconds(2);
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken("swiftboot");
        Assertions.assertTrue(jwtTokenProvider.validateToken(refreshToken.getTokenValue()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(Exception.class, () -> jwtTokenProvider.validateToken(refreshToken.getTokenValue()));
    }

    @Test
    public void testRefreshAccessToken() {
        jwtConfigBean.setRefreshTokenExpirationSeconds(2);
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken("swiftboot");
        Assertions.assertTrue(jwtTokenProvider.validateToken(refreshToken.getTokenValue()));
        // check if the refresh token is available
        AccessToken newAccessToken = jwtTokenProvider.refreshAccessToken(refreshToken.getTokenValue());
        Assertions.assertNotNull(newAccessToken);
        Assertions.assertTrue(jwtTokenProvider.validateToken(newAccessToken.getTokenValue()));
        // check if the refresh token is unavailable
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(Exception.class, () -> jwtTokenProvider.validateToken(refreshToken.getTokenValue()));

    }
}
