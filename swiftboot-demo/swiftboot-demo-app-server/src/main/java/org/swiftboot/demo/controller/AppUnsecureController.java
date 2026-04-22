package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.common.auth.annotation.IfNecessary;
import org.swiftboot.common.auth.annotation.UserId;
import org.swiftboot.web.response.Response;

@Tag(name = "App Unsecure Domain")
@Controller
@RequestMapping("/app/unsecure")
@ResponseBody
public class AppUnsecureController {

    private static final Logger log = LoggerFactory.getLogger(AppUnsecureController.class);

    /**
     * Returns data no matter the JWT is valid or not.
     *
     * @param userId
     * @return
     */
    @Operation(description = "Unsecure domain without handling token")
    @GetMapping(value = "token-ignore")
    public Response<String> unsecureNoToken(@UserId String userId) {
        log.info("> /app/unsecure");
        log.info("User ID: " + userId); // the userId would be null if jwt is invalid.
        return new Response<>("OK");
    }

    /**
     * Returns 401 if JWT is invalid; passes with no user id if JWT is not provided.
     *
     * @param userId
     * @return
     */
    @Operation(description = "Unsecure domain but still handles token")
    @GetMapping(value = "token-sensitive")
    public Response<String> unsecureWithToken(@UserId @IfNecessary String userId) {
        log.info("> /app/unsecure");
        log.info("User ID: " + userId);
        return Response.builder(String.class).data("OK with user ID: " + userId).ok().build();
    }
}
