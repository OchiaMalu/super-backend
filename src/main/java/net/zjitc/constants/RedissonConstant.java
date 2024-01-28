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
    /**
     * 解散过期团队锁
     */
    public static final String DISBAND_EXPIRED_TEAM_LOCK = "super:disbandTeam:lock";
    /**
     * 用户推荐锁
     */
    public static final String USER_RECOMMEND_LOCK = "super:user:recommend:lock";
    /**
     * 博客点赞锁
     */
    public static final String BLOG_LIKE_LOCK = "super:blog:like:lock:";
    /**
     * 评论点赞锁
     */
    public static final String COMMENTS_LIKE_LOCK = "super:comments:like:lock:";
    /**
     * 默认等待时间
     */
    public static final long DEFAULT_WAIT_TIME = 0;
    /**
     * 违约租赁时间
     */
    public static final long DEFAULT_LEASE_TIME = -1;

}
