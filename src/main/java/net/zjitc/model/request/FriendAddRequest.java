package net.zjitc.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class FriendAddRequest implements Serializable {
    private static final long serialVersionUID = 1472823745422792988L;

    private Long id;
    /**
     * 接收申请的用户id
     */
    private Long receiveId;

    /**
     * 好友申请备注信息
     */
    private String remark;
}
