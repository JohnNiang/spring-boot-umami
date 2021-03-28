package me.johnniang.umami.model;

import lombok.Data;

/**
 * Access token.
 *
 * @author johnniang
 */
@Data
public class AccessToken {

    public AccessToken(String token) {
        this.token = token;
    }

    private String token;

}
