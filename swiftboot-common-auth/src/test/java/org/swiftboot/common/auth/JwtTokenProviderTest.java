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

    private static final String SECRET = "d9c0fd4d4aa9c7c228cd8194efcff1d0ec200310ba41e106a198d05025cea15f147f2bfe6d5a415656bf53c4b843d269ccf7921d65fc652f00008eda95393e709ec0d4c12b6f1fd18da0d470ca9750313c082d49f869311029663deb7ae7864791d22ab3424b8a85f6c63f73b86c49d6d50d1b21527c9274e4f1ff2143fe5b28bf69d14688b15d4711f0c03c25db2624ee830a3450693ad5e5070d4a7a1fc046ffe1cd1e9fa0b32f58b041e2e859bfdf92eff72a9f1ebd387470429dbba6af57b30b0ba76f68e53ccb87949972649c34322811f1b3966ee87ffa9dc38d6a2737de88f0a9aaa704d52b25d1f3ab7048c5e55e140cdc71aae0b35644ac7834c19f";

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
        Assertions.assertTrue(jwtTokenProvider.validateToken(accessToken.tokenValue()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(Exception.class, () -> jwtTokenProvider.validateToken(accessToken.tokenValue()));
    }

    @Test
    public void testGetUsername() {
        String uid = IdUtils.makeID("uid");
        AccessToken accessToken = jwtTokenProvider.generateAccessToken(uid, "swiftboot");
        System.out.println(accessToken);
        Assertions.assertEquals(uid, jwtTokenProvider.getUserId(accessToken.tokenValue()));
        Assertions.assertEquals("swiftboot", jwtTokenProvider.getAddition(accessToken.tokenValue(), JwtTokenProvider.USERNAME_KEY));
        Assertions.assertEquals("swiftboot", jwtTokenProvider.getUsername(accessToken.tokenValue()));
    }

    @Test
    public void testRefreshToken() {
        jwtConfigBean.setRefreshTokenExpirationSeconds(2);
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken("swiftboot");
        Assertions.assertTrue(jwtTokenProvider.validateToken(refreshToken.tokenValue()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(Exception.class, () -> jwtTokenProvider.validateToken(refreshToken.tokenValue()));
    }

    @Test
    public void testRefreshAccessToken() {
        jwtConfigBean.setRefreshTokenExpirationSeconds(2);
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken("swiftboot");
        Assertions.assertTrue(jwtTokenProvider.validateToken(refreshToken.tokenValue()));
        // check if the refresh token is available
        AccessToken newAccessToken = jwtTokenProvider.refreshAccessToken(refreshToken.tokenValue());
        Assertions.assertNotNull(newAccessToken);
        Assertions.assertTrue(jwtTokenProvider.validateToken(newAccessToken.tokenValue()));
        // check if the refresh token is unavailable
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(Exception.class, () -> jwtTokenProvider.validateToken(refreshToken.tokenValue()));

    }
}
