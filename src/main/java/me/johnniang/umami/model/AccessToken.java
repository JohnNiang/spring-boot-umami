package me.johnniang.umami.model;

import lombok.Data;

import java.time.Instant;

/**
 * Access token.
 *
 * @author johnniang
 */
@Data
public class AccessToken {

    private String token;

    private Long exp;

    public AccessToken(String token, Instant expirationTime) {
        this.token = token;
        this.exp = expirationTime.toEpochMilli() / 1000;
    }

}
