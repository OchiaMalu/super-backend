package net.zjitc.constants;

/**
 * redisson常量
 *
 * @author OchiaMalu
 * @date 2023/06/22
 */
public interface RedissonConstant {
    /**
     * 应用锁
     */
    String APPLY_LOCK = "super:apply:lock:";
    String DISBAND_EXPIRED_TEAM_LOCK = "super:disbandTeam:lock";
    String USER_RECOMMEND_LOCK = "super:user:recommend:lock";
    String BLOG_LIKE_LOCK = "super:blog:like:lock:";
    String COMMENTS_LIKE_LOCK = "super:comments:like:lock:";
}
