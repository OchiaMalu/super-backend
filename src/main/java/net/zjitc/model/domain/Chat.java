package net.zjitc.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 聊天消息表
 * @TableName chat
 */
@TableName(value ="chat")
@Data
public class Chat implements Serializable {
    /**
     * 聊天记录id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发送消息id
     */
    private Long fromId;

    /**
     * 接收消息id
     */
    private Long toid;

    /**
     * 
     */
    private String text;

    /**
     * 聊天类型 1-私聊 2-群聊
     */
    private Integer chatType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Long teamId;

    /**
     * 
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}