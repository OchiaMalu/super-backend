package net.zjitc.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName tag
 */
@TableName(value ="tag")
@Data
public class Tag implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 上传用户id
     */
    private Long userId;

    /**
     * 父标签id
     */
    private Long parentId;

    /**
     * 0-不是父标签，1-父标签
     */
    private Integer isParent;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}