package me.johnniang.umami.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import me.johnniang.umami.cache.Cache;
import me.johnniang.umami.model.AccessToken;
import me.johnniang.umami.model.AuthRequest;
import me.johnniang.umami.repository.AccountRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Auth controller.
 *
 * @author johnniang
 */
@RestController
@RequestMapping("/api/auth")
class AuthController {

    private final AccountRepository accountRepository;

    private final Cache cache;

    AuthController(AccountRepository accountRepository, Cache cache) {
        this.accountRepository = accountRepository;
        this.cache = cache;
    }

    @RequestMapping("/login")
    AccessToken login(@RequestBody @Validated AuthRequest auth, HttpServletResponse response) {
        return accountRepository.findByUsername(auth.getUsername()).flatMap(account -> {
            // validate password
            BCrypt.Result verified = BCrypt.verifyer().verify(auth.getPassword().getBytes(StandardCharsets.UTF_8), account.getPassword().getBytes(StandardCharsets.UTF_8));
            if (!verified.verified) {
                return Optional.empty();
            }
            // build access token
            String token = UUID.randomUUID().toString();
            Duration timeout = Duration.ofDays(30);
            cache.put(token, account, timeout);
            return Optional.of(new AccessToken(token, Instant.now().plus(timeout)));
        }).orElseThrow(() -> {
            throw new IllegalArgumentException("Username was not found or password was incorrect!");
        });
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
