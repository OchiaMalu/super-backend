package net.zjitc.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 好友申请管理表
 * @TableName friends
 */
@TableName(value ="friends")
@Data
public class Friends implements Serializable {
    /**
     * 好友申请id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发送申请的用户id
     */
    private Long fromId;

    /**
     * 接收申请的用户id 
     */
    private Long receiveId;

    /**
     * 是否已读(0-未读 1-已读)
     */
    private Integer isRead;

    /**
     * 申请状态 默认0 （0-未通过 1-已同意 2-已过期 3-已撤销）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 好友申请备注信息
     */
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}