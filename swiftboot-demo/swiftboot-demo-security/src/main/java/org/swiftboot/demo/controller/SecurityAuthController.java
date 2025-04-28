package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
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
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.annotation.Token;
import org.swiftboot.common.auth.request.NamePasswordLoginRequest;
import org.swiftboot.common.auth.request.RefreshTokenRequest;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.common.auth.token.RefreshToken;
import org.swiftboot.demo.dto.AuthenticatedDto;
import org.swiftboot.demo.dto.CustomUserDetails;
import org.swiftboot.demo.model.UserEntity;
import org.swiftboot.demo.repository.UserRepository;
import org.swiftboot.web.response.Response;

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

    @Resource
    private UserRepository userRepository;

    @Operation(description = "Signin with username and password")
    @PostMapping(value = "signin")
    public Response<AuthenticatedDto> adminUserSignin(
            @RequestBody NamePasswordLoginRequest authRequest, HttpServletResponse response) {
        log.info("> /security/auth/signin");
        String loginName = authRequest.getLoginName();
        String loginPwd = authRequest.getLoginPwd();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginName, loginPwd);
        log.info("user %s login...".formatted(loginName));
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);

        if (authentication.getPrincipal() instanceof CustomUserDetails ud) {
            log.debug(ud.getId());
            log.debug(ud.getUsername());
            AccessToken accessToken = jwtTokenProvider.generateAccessToken(ud.getId(), loginName);
            RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(ud.getId());

            jwtService.saveJwtAuthentication(accessToken, refreshToken);

            log.info("User login in: %s".formatted(loginName));
            AuthenticatedDto authenticatedDto = new AuthenticatedDto(accessToken.tokenValue(), accessToken.expiresAt(),
                    refreshToken.tokenValue(), refreshToken.expiresAt());
            // TODO
            authenticatedDto.setRole(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
            return new Response<>(authenticatedDto);
        }
        else {
            throw new RuntimeException("No user detail found");
        }
    }

    @Operation(description = "Refresh access token by refresh token")
    @PostMapping("refresh")
    public ResponseEntity<Response<AuthenticatedDto>> refresh(@RequestBody RefreshTokenRequest refreshTokenCommand) {
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

                    AuthenticatedDto authenticatedDto = new AuthenticatedDto(jwtAuthentication.getAccessToken(), jwtAuthentication.getRefreshToken());

                    return new ResponseEntity<>(new Response<>(authenticatedDto), HttpStatus.OK);
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


    @Operation(description = "for testing access token revocation only")
    @GetMapping("revoke_token")
    public Response<String> revokeToken(@Token String accessToken) {
        if (jwtService.revokeAuthenticationByAccessToken(accessToken)) {
            return new Response<>("OK");
        }
        else {
            return new Response<>("Fail");
        }
    }

    @Operation(description = "")
    @GetMapping("logout_success")
    public Response<String> logoutSuccess(@Token String accessToken) {
        String ret = jwtService.revokeAuthenticationByAccessToken(accessToken) ? "success" : "fail";
//        String userId = jwtTokenProvider.getUserId(accessToken);
//        if (!refreshTokenService.revokeRefreshToken(userId)) {
//            return new HttpResponse<>("failed to logout");
//        }
        return new Response<>("logout " + ret);
    }
}
