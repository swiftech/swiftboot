package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.JwtUtils;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.common.auth.token.RefreshToken;
import org.swiftboot.demo.command.AuthCommand;
import org.swiftboot.demo.command.RefreshTokenCommand;
import org.swiftboot.demo.dto.AuthenticatedDto;
import org.swiftboot.demo.dto.CustomUserDetails;
import org.swiftboot.demo.dto.RefreshTokenDto;
import org.swiftboot.demo.model.UserEntity;
import org.swiftboot.demo.repository.UserRepository;
import org.swiftboot.web.result.HttpResponse;

import java.util.Optional;
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
    private JwtService jwtService;

    @Resource
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

//    @Resource
//    private RevokedTokenService revokedTokenService;
//
//    @Resource
//    private RefreshTokenService refreshTokenService;

    @Operation(description = "Signin with username and password")
    @PostMapping(value = "signin")
    @ResponseBody
    public HttpResponse<AuthenticatedDto> adminUserSignin(
            @RequestBody AuthCommand command, HttpServletResponse response) {
        log.info("> /security/auth/signin");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(command.getUserName(), command.getPassword());
        log.info("user %s login...".formatted(command.getUserName()));
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);

        if (authentication.getPrincipal() instanceof CustomUserDetails ud) {
            log.debug(ud.getId());
            log.debug(ud.getUsername());
            AccessToken accessToken = jwtTokenProvider.generateAccessToken(ud.getId(), command.getUserName());
            RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(ud.getId());

            jwtService.saveJwtAuthentication(accessToken, refreshToken);
//            refreshTokenService.saveRefreshToken(ud.getId(), refreshToken.tokenValue(), refreshToken.expiresAt());

            log.info("User login in: %s".formatted(command.getUserName()));
            AuthenticatedDto authenticatedDto = new AuthenticatedDto(accessToken.tokenValue(), accessToken.expiresAt(),
                    refreshToken.tokenValue(), refreshToken.expiresAt());
            authenticatedDto.setRole(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
            return new HttpResponse<>(authenticatedDto);
        }
        else {
            throw new RuntimeException("No user id");
        }
    }

    @Operation(description = "Refresh access token by refresh token")
    @PostMapping("refresh")
    @ResponseBody
    public ResponseEntity<HttpResponse<RefreshTokenDto>> refresh(@RequestBody RefreshTokenCommand refreshTokenCommand) {
        try {
            String refreshToken = refreshTokenCommand.getRefreshToken();
            if (StringUtils.isBlank(refreshToken) || !jwtTokenProvider.validateToken(refreshToken)) {
                throw new RuntimeException("Refresh Token is invalid");
            }
            String userId = jwtTokenProvider.getUserId(refreshToken);

            if (jwtService.isRevokedRefreshToken(refreshToken)) {
                log.warn("Refresh token is revoked: {}", StringUtils.abbreviate(refreshToken, "..", 64));
            }
            else {
                Optional<UserEntity> byId = userRepository.findById(userId);
                if (byId.isPresent()) {
                    UserEntity userEntity = byId.get();
                    JwtAuthentication jwtAuthentication = this.generateTokens(userEntity);

                    // save new refresh token
                    jwtService.saveJwtAuthentication(jwtAuthentication);

                    // revoke used refresh token
                    jwtService.revokeAuthenticationByRefreshToken(refreshToken);

                    // TODO use AuthenticatedDto instead
                    RefreshTokenDto result = new RefreshTokenDto(jwtAuthentication.getRefreshToken());

                    return new ResponseEntity<>(new HttpResponse<>(result), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private JwtAuthentication generateTokens(UserEntity appUserEntity) {
        AccessToken accessToken = jwtTokenProvider.generateAccessToken(appUserEntity.getId(), appUserEntity.getLoginName());
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(appUserEntity.getId());
        return new JwtAuthentication(accessToken, refreshToken);
    }


//    @ApiOperation("for testing access token revocation")
//    @GetMapping("revoke_token")
//    @ResponseBody
//    public HttpResponse<String> revokeToken(@RequestHeader("Authorization") String authorizationHeader) {
//        String tokenValue = JwtUtils.extractBearerToken(authorizationHeader);
//        String ret = revokedTokenDaoStub.revokeToken(tokenValue) ? "success" : "fail";
//        return new HttpResponse<>(ret);
//    }

    @Operation(description = "")
    @GetMapping("logout_success")
    @ResponseBody
    public HttpResponse<String> logoutSuccess(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = JwtUtils.extractBearerToken(authorizationHeader);
        String ret = jwtService.revokeAuthenticationByAccessToken(accessToken) ? "success" : "fail";
//        String userId = jwtTokenProvider.getUserId(accessToken);
//        if (!refreshTokenService.revokeRefreshToken(userId)) {
//            return new HttpResponse<>("failed to logout");
//        }
        return new HttpResponse<>("logout " + ret);
    }
}
