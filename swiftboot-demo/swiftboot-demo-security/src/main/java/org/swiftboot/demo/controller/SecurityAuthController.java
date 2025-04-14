package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.JwtUtils;
import org.swiftboot.demo.command.AuthCommand;
import org.swiftboot.demo.command.RefreshTokenCommand;
import org.swiftboot.demo.result.AuthResult;
import org.swiftboot.demo.result.RefreshTokenResult;
import org.swiftboot.security.RefreshTokenDao;
import org.swiftboot.security.RevokedTokenDao;
import org.swiftboot.web.result.HttpResponse;

import java.util.stream.Collectors;

/**
 * @author swiftech
 */
@Tag(name = "Security Authentication", description = "Authentication based on Spring Security")
@Controller
@RequestMapping("/security/auth")
@ResponseBody
public class SecurityAuthController {
    private static final Logger log = LoggerFactory.getLogger(SecurityAuthController.class);

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Resource
    private RevokedTokenDao revokedTokenDao;

    @Resource
    private RefreshTokenDao refreshTokenDao;

    @Operation(description = "Signin with username and password")
    @PostMapping(value = "signin")
    @ResponseBody
    public HttpResponse<AuthResult> adminUserSignin(
            @RequestBody AuthCommand command, HttpServletResponse response) {
        log.info("> /security/auth/signin");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(command.getUserName(), command.getPassword());
        log.info("user %s login...".formatted(command.getUserName()));
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);

//        authentication.getPrincipal()

        String accessToken = jwtTokenProvider.generateAccessToken(command.getUserName());
        String refreshToken = jwtTokenProvider.generateRefreshToken(command.getUserName());

        refreshTokenDao.saveRefreshToken(command.getUserName(), refreshToken);

        log.info("User longed in: %s".formatted(command.getUserName()));
        AuthResult authResult = new AuthResult();
        authResult.setAccessToken(accessToken);
        authResult.setRefreshToken(refreshToken);
        authResult.setRole(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
        return new HttpResponse<>(authResult);
    }

    @Operation(description = "Refresh access token by refresh token")
    @PostMapping("refresh")
    @ResponseBody
    public ResponseEntity<HttpResponse<RefreshTokenResult>> refresh(@RequestBody RefreshTokenCommand refreshTokenCommand) {
        try {
            String username = jwtTokenProvider.getUsername(refreshTokenCommand.getRefreshToken());
            if (!refreshTokenDao.isValidRefreshToken(username)) {
                return new ResponseEntity<>(new HttpResponse<>(new RefreshTokenResult("Refresh Token has been revoked.")), HttpStatus.UNAUTHORIZED);
            }
            String accessToken = jwtTokenProvider.refreshAccessToken(refreshTokenCommand.getRefreshToken());
            RefreshTokenResult result = new RefreshTokenResult(accessToken);
            return new ResponseEntity<>(new HttpResponse<>(result), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


//    @ApiOperation("for testing access token revocation")
//    @GetMapping("revoke_token")
//    @ResponseBody
//    public HttpResponse<String> revokeToken(@RequestHeader("Authorization") String authorizationHeader) {
//        String accessToken = JwtUtils.extractBearerToken(authorizationHeader);
//        String ret = revokedTokenDaoStub.revokeToken(accessToken) ? "success" : "fail";
//        return new HttpResponse<>(ret);
//    }

    @Operation(description = "")
    @GetMapping("logout_success")
    @ResponseBody
    public HttpResponse<String> logoutSuccess(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = JwtUtils.extractBearerToken(authorizationHeader);
        String ret = revokedTokenDao.revokeToken(accessToken) ? "success" : "fail";
        String username = jwtTokenProvider.getUsername(accessToken);
        if (!refreshTokenDao.revokeRefreshToken(username)) {
            return new HttpResponse<>("failed to logout");
        }
        return new HttpResponse<>("logout " + ret);
    }
}
