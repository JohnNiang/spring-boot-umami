package me.johnniang.umami.security;

/**
 * Security context holder.
 *
 * @author johnniang
 */
public abstract class SecurityContextHolder {

    private static final ThreadLocal<RequestContext> contextHolder = new ThreadLocal<>();

    public static void setContext(RequestContext requestContext) {
        if (requestContext == null) {
            resetContext();
        } else {
            contextHolder.set(requestContext);
        }
    }

    public static RequestContext getContext() {
        return contextHolder.get();
    }

    public static void resetContext() {
        contextHolder.remove();
    }

}
