package me.johnniang.umami.model;

import lombok.Data;

/**
 * Auth param.
 *
 * @author johnniang
 */
@Data
public class AuthRequest {

    private String username;

    private String password;

}
