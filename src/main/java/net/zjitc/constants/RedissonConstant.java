package net.zjitc.constants;

/**
 * redisson常量
 *
 * @author OchiaMalu
 * @date 2023/06/22
 */
public final class RedissonConstant {
    private RedissonConstant() {
    }

    /**
     * 应用锁
     */
    public static final String APPLY_LOCK = "super:apply:lock:";
    public static final String DISBAND_EXPIRED_TEAM_LOCK = "super:disbandTeam:lock";
    public static final String USER_RECOMMEND_LOCK = "super:user:recommend:lock";
    public static final String BLOG_LIKE_LOCK = "super:blog:like:lock:";
    public static final String COMMENTS_LIKE_LOCK = "super:comments:like:lock:";
}
