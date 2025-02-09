package org.swiftboot.common.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

/**
 *
 * @since 3.0.0
 * @see JwtConfigBean
 */
public class JwtTokenProvider {

    private final JwtConfigBean jwtConfig;

    public JwtTokenProvider(JwtConfigBean jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    // generate JWT token
    public String generateToken(String username) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtConfig.getExpirationSeconds() * 1000);
        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key())
                .compact();
        return token;
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
        Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parse(token);
        return true;

    }
}
