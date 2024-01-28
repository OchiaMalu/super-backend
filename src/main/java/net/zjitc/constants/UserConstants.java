package net.zjitc.constants;

/**
 * 用户常量
 *
 * @author OchiaMalu
 * @date 2024/01/25
 */

public final class UserConstants {
    private UserConstants() {
    }

    /**
     * 用户登录态键
     */
    public static final String USER_LOGIN_STATE = "userLoginState";

    /**
     * 最小帐户长度
     */
    public static final int MINIMUM_ACCOUNT_LEN = 4;

    /**
     * 最小密码长度
     */
    public static final int MINIMUM_PASSWORD_LEN = 4;

    /**
     * 默认权限
     */
    public static final int DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    public static final int ADMIN_ROLE = 1;
}
