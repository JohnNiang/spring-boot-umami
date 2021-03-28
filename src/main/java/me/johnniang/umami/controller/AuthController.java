package me.johnniang.umami.controller;

import me.johnniang.umami.model.AccessToken;
import me.johnniang.umami.model.AuthRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Auth controller.
 *
 * @author johnniang
 */
@RestController
@RequestMapping("/api/auth")
class AuthController {

    @RequestMapping("/login")
    AccessToken login(@RequestBody @Validated AuthRequest auth) {
        //TODO Handle login
        return new AccessToken("jwt token");
    }

    @RequestMapping("/logout")
    void logout() {
        //TODO Handle logout
    }

    @RequestMapping("/verify")
    void verify() {
        //TODO Handle verify
    }
}
