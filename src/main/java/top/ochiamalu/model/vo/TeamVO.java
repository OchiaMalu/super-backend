package top.ochiamalu.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 团队签证官
 *
 * @author OchiaMalu
 * @date 2023/06/22
 */
@Data
@ApiModel(value = "队伍返回")
public class TeamVO implements Serializable {
    private static final long serialVersionUID = 6986365414601034543L;
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 队伍名称
     */
    @ApiModelProperty(value = "队伍名称")
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 封面图片
     */
    @ApiModelProperty(value = "封面图片")
    private String coverImage;

    /**
     * 最大人数
     */
    @ApiModelProperty(value = "最大人数")
    private Integer maxNum;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    private Date expireTime;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "队长id")
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date updateTime;

    /**
     * 创建人用户信息
     */
    @ApiModelProperty(value = "创建人")
    private UserVO createUser;

    private String leaderName;
    /**
     * 已加入的用户数
     */
    @ApiModelProperty(value = "已加入的用户数")
    private Long hasJoinNum;

    /**
     * 是否已加入队伍
     */
    @ApiModelProperty(value = "是否已加入队伍")
    private boolean hasJoin = false;

    private List<String> joinedUserAvatars;
}
