package me.johnniang.umami.model;

import lombok.Data;

/**
 * Account request.
 *
 * @author johnniang
 */
@Data
public class AccountRequest {

    private Integer userId;

    private String username;

    private String password;

}
