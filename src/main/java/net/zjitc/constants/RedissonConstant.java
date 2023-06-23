package net.zjitc.constants;

/**
 * redisson常量
 *
 * @author 林哲好
 * @date 2023/06/22
 */
public interface RedissonConstant {
    /**
     * 应用锁
     */
    String APPLY_LOCK = "super:apply:lock:";

    String DISBAND_EXPIRED_TEAM_LOCK = "super:disbandTeam:lock";
    String USER_RECOMMEND_LOCK = "super:user:recommend:lock";
}
