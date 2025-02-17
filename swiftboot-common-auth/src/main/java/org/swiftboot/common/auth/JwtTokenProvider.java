package org.swiftboot.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

/**
 * @see JwtConfigBean
 * @since 3.0.0
 */
public class JwtTokenProvider {

    private final JwtConfigBean jwtConfig;

    public JwtTokenProvider(JwtConfigBean jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    // generate JWT token
    public String generateAccessToken(String username) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtConfig.getAccessTokenExpirationSeconds() * 1000);
        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }

    public String generateRefreshToken(String username) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtConfig.getRefreshTokenExpirationSeconds() * 1000);
        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }

    public String refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new IllegalStateException("Invalid refresh token");
        }
        String username = this.getUsername(refreshToken);
        if (StringUtils.isBlank(username)) {
            throw new IllegalStateException("Invalid username");
        }
        return this.generateAccessToken(username);
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()
        ));
    }

    // get username from JWT token
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // validate JWT token
    public boolean validateToken(String token) {
        Claims body = Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Date expiration = body.getExpiration();
        String username = body.getSubject();
        return StringUtils.isNotBlank(username) && !expiration.before(new Date());
    }
}
