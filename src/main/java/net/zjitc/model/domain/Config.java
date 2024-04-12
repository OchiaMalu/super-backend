package net.zjitc.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 配置
 *
 * @author OchiaMalu
 * @date 2024/04/12
 */
@TableName(value = "config")
@Data
@ApiModel(value = "配置")
public class Config implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 数据
     */
    @ApiModelProperty(value = "数据")
    private String value;

    /**
     * 数据类型
     * 0-通知栏 2-轮播图
     */
    @ApiModelProperty(value = "数据类型")
    private Integer type;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
