package me.johnniang.umami.security;

import me.johnniang.umami.entity.Account;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Authentication context interface.
 *
 * @author johnniang
 */
public interface RequestContext {

    String getToken();

    Account getAccount();

    default TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    default Locale getLocale() {
        return Locale.getDefault();
    }

    class AnonymousAccount extends Account {

        public AnonymousAccount() {
            setId(0);
            setUsername("Anonymous");
            setAdmin(false);
        }
    }
}
