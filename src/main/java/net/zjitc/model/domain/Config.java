package net.zjitc.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName config
 */
@TableName(value = "config")
@Data
public class Config implements Serializable {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 数据
     */
    private String value;

    /**
     * 0-通知栏 2-轮播图
     */
    private Integer type;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}