package net.zjitc.constants;

/**
 * 团队常量
 *
 * @author OchiaMalu
 * @date 2024/01/28
 */
public final class TeamConstants {
    private TeamConstants() {
    }

    /**
     * 最大团队数量
     */
    public static final int MAXIMUM_TEAM_NUM = 5;
    /**
     * 最大成员数量
     */
    public static final int MAXIMUM_MEMBER_NUM = 20;
    /**
     * 最大标题长度
     */
    public static final int MAXIMUM_TITLE_LEN = 20;
    /**
     * 最大描述长度
     */
    public static final int MAXIMUM_DESCRIPTION_LEN = 512;
    /**
     * 最大密码长度
     */
    public static final int MAXIMUM_PASSWORD_LEN = 32;

    /**
     * 最大加入团队
     */
    public static final int MAXIMUM_JOINED_TEAM = 5;

    /**
     * 队伍卡片显示已加入队员头像的最大数量
     */
    public static final int MAXIMUM_JOINED_USER_AVATAR_NUM = 3;
}
