package org.swiftboot.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @see JwtConfigBean
 * @since 3.0.0
 */
public class JwtTokenProvider {

    public static final String USERNAME_KEY = "username";
    private final JwtConfigBean jwtConfig;

    public JwtTokenProvider(JwtConfigBean jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    // generate JWT token
    public String generateAccessToken(String userId) {
        return this.generateAccessToken(userId, Collections.EMPTY_MAP);
    }

    public String generateAccessToken(String userId, String userName) {
        return this.generateAccessToken(userId, StringUtils.isBlank(userName) ? null: Collections.singletonMap(USERNAME_KEY, userName));
    }

    public String generateAccessToken(String userId, String additionKey, Object additionValue) {
        return this.generateAccessToken(userId, Collections.singletonMap(additionKey, additionValue));
    }

    public String generateAccessToken(String userId, String userName, Map<String, Object> additions) {
        if (additions == null) additions = new HashMap<>();
        if (StringUtils.isNotBlank(userName)) additions.put(USERNAME_KEY, userName);
        return this.generateAccessToken(userId, additions);
    }

    public String generateAccessToken(String userId, Map<String, Object> additions) {
        if (StringUtils.isBlank(userId)) {
            throw new RuntimeException("User ID is required to generate access token");
        }
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtConfig.getAccessTokenExpirationSeconds() * 1000);
        JwtBuilder builder = Jwts.builder();
        builder.subject(userId)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key());
        if (additions != null && !additions.isEmpty()) {
            additions.keySet().forEach(k -> builder.claim(k, additions.get(k)));
        }
        return builder.compact();
    }

    public String generateRefreshToken(String userId) {
        return this.generateRefreshToken(userId, null);
    }

    public String generateRefreshToken(String userId, Map<String, Object> additions) {
        if (StringUtils.isBlank(userId)) {
            throw new RuntimeException("User ID is required to generate access token");
        }
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtConfig.getRefreshTokenExpirationSeconds() * 1000);
        JwtBuilder builder = Jwts.builder();
        builder.subject(userId)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key());
        if (additions != null && !additions.isEmpty()) {
            additions.keySet().forEach(k -> builder.claim(k, additions.get(k)));
        }
        return builder.compact();
    }

    public String refreshAccessToken(String refreshToken) {
        return refreshAccessToken(refreshToken, null);
    }

    public String refreshAccessToken(String refreshToken, Map<String, Object> additions) {
        if (!validateToken(refreshToken)) {
            throw new IllegalStateException("Invalid refresh token");
        }
        String userId = this.getUserId(refreshToken);
        if (StringUtils.isBlank(userId)) {
            throw new IllegalStateException("Invalid refresh token");
        }
        return this.generateAccessToken(userId, additions);
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()
        ));
    }

    // get user ID from JWT token
    public String getUserId(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getUsername(String token) {
        Object addition = getAddition(token, USERNAME_KEY);
        return addition == null ? "" : addition.toString();
    }

    public Date getExpireTime(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    // get username from JWT token
    public Object getAddition(String token, String key) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        if (claims == null || !claims.containsKey(key)) return null;
        return claims.get(key);
    }

    public Map<String, Object> getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // validate JWT token
    public boolean validateToken(String token) {
        Claims body = Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Date expiration = body.getExpiration();
        Object userId = body.getSubject();
        return userId != null && StringUtils.isNotBlank(userId.toString())
                && expiration.after(new Date());
//        boolean noUid = Objects.isNull(userId) || StringUtils.isBlank(userId.toString());
//        boolean noUsername = Objects.isNull(username) || StringUtils.isBlank(username.toString());
//        return !(noUid && noUsername) && expiration.after(new Date());
    }
}
