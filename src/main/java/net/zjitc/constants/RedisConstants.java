package net.zjitc.constants;

/**
 * Redis常量
 *
 * @author OchiaMalu
 * @date 2023/06/22
 */
public final class RedisConstants {
    private RedisConstants() {
    }

    public static final String LOGIN_USER_KEY = "super:login:token:";

    public static final Long LOGIN_USER_TTL = 15L;
    /**
     * 注册验证码键
     */
    public static final String REGISTER_CODE_KEY = "super:register:";
    /**
     * 注册验证码过期时间
     */
    public static final Long REGISTER_CODE_TTL = 15L;
    /**
     * 用户更新电话键
     */
    public static final String USER_UPDATE_PHONE_KEY = "suer:user:update:phone:";
    /**
     * 用户更新电话过期时间
     */
    public static final Long USER_UPDATE_PHONE_TTL = 15L;
    /**
     * 用户更新邮件键
     */
    public static final String USER_UPDATE_EMAIL_KEY = "suer:user:update:email:";
    /**
     * 用户更新邮件过期时间
     */
    public static final Long USER_UPDATE_EMAIL_TTL = 15L;
    /**
     * 用户忘记密码键
     */
    public static final String USER_FORGET_PASSWORD_KEY = "super:user:forget:";
    /**
     * 用户忘记密码过期时间
     */
    public static final Long USER_FORGET_PASSWORD_TTL = 15L;
    /**
     * 博客推送键
     */
    public static final String BLOG_FEED_KEY = "super:feed:blog:";
    /**
     * 新博文消息键
     */
    public static final String MESSAGE_BLOG_NUM_KEY = "super:message:blog:num:";
    /**
     * 新点赞消息键
     */
    public static final String MESSAGE_LIKE_NUM_KEY = "super:message:like:num:";
    /**
     * 用户推荐缓存
     */
    public static final String USER_RECOMMEND_KEY = "super:recommend:";

    /**
     * 最小缓存随机时间
     */
    public static final int MINIMUM_CACHE_RANDOM_TIME = 2;
    /**
     * 最大缓存随机时间
     */
    public static final int MAXIMUM_CACHE_RANDOM_TIME = 3;
    /**
     * 缓存时间偏移
     */
    public static final int CACHE_TIME_OFFSET = 10;
}
