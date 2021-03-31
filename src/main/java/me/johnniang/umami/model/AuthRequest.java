package me.johnniang.umami.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Auth param.
 *
 * @author johnniang
 */
@Data
public class AuthRequest {

    @NotBlank(message = "Username must not be blank.")
    private String username;

    @NotBlank(message = "Password must not be blank")
    private String password;

}
