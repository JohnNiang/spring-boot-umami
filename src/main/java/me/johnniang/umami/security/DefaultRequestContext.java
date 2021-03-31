package me.johnniang.umami.security;

import me.johnniang.umami.entity.Account;

/**
 * Default authentication context.
 *
 * @author johnniang
 */
public class DefaultRequestContext implements RequestContext {

    private final String token;

    private final Account account;

    public DefaultRequestContext(String token, Account account) {
        this.token = token;
        this.account = account;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public Account getAccount() {
        return account;
    }
}
