package org.swiftboot.demo.controller;

import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.demo.command.AuthCommand;
import org.swiftboot.demo.result.AuthResult;
import org.swiftboot.web.result.HttpResponse;

/**
 * @author swiftech
 */
@Api(tags = {"Authentication based on Spring Security"})
@Controller
@RequestMapping("/security/auth")
@ResponseBody
public class SecurityAuthController {
    private static final Logger log = LoggerFactory.getLogger(SecurityAuthController.class);

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @RequestMapping(value = "signin", method = RequestMethod.POST)
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

        String accessToken = jwtTokenProvider.generateToken(command.getUserName());

        log.info("User longed in: %s".formatted(command.getUserName()));
        AuthResult authResult = new AuthResult();
        authResult.setAccessToken(accessToken);
        return new HttpResponse<>(authResult);
    }

//    @ApiOperation(notes = "user signout", value = "user signout")
//    @RequestMapping(value = "signout", method = RequestMethod.POST)
//    public HttpResponse<AdminUserSignoutResult> adminUserSignout(
//            @RequestBody AdminUserSignoutCommand command,
//            HttpServletResponse response) {
//        log.info("> /security/auth/signout");
//        return new HttpResponse<>(null);
//    }
}
