package net.zjitc.constants;

/**
 * 好友常量
 *
 * @author OchiaMalu
 * @date 2023/06/22
 */
public final class FriendConstant {
    private FriendConstant() {
    }

    /**
     * 默认状态 未处理
     */
    public static final int DEFAULT_STATUS = 0;
    /**
     * 已同意
     */
    public static final int AGREE_STATUS = 1;
    /**
     * 已过期
     */
    public static final int EXPIRED_STATUS = 2;

    /**
     * 撤销
     */
    public static final int REVOKE_STATUS = 3;
    /**
     * 未读
     */
    public static final int NOT_READ = 0;

    /**
     * 已读
     */
    public static final int READ = 1;
    /**
     * 最大备注长度
     */
    public static final int MAXIMUM_REMARK_LENGTH = 120;
    /**
     * 最长申请时间
     */
    public static final int MAXIMUM_APPLY_TIME = 3;
}
