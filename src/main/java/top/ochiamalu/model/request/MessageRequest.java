package top.ochiamalu.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息请求
 *
 * @author OchiaMalu
 * @date 2023/06/19
 */
@Data
@ApiModel(value = "消息请求")
public class MessageRequest implements Serializable {
    /**
     * 串行版本uid
     */
    private static final long serialVersionUID = 1324635911327892058L;
    /**
     * 为id
     */
    @ApiModelProperty(value = "接收id")
    private Long toId;
    /**
     * 团队id
     */
    @ApiModelProperty(value = "队伍id")
    private Long teamId;
    /**
     * 文本
     */
    @ApiModelProperty(value = "文本")
    private String text;
    /**
     * 消息类型
     */
    @ApiModelProperty(value = "消息类型")
    private String messageType;
    /**
     * 聊天类型
     */
    @ApiModelProperty(value = "聊天类型")
    private Integer chatType;
    /**
     * 是管理
     */
    @ApiModelProperty(value = "是否为管理员")
    private boolean isAdmin;
}
