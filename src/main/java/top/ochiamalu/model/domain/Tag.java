package top.ochiamalu.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 标签
 *
 * @author ochiamalu
 * @date 2025/04/15
 */
@TableName(value = "tag")
@Data
public class Tag {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 标签名
     */
    private String text;

    /**
     * 子标签
     */
    private String children;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}