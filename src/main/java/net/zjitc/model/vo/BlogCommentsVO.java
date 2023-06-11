package net.zjitc.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.zjitc.model.domain.BlogComments;

import java.io.Serializable;
@Data
@ApiModel(value = "博文评论返回")
public class BlogCommentsVO extends BlogComments implements Serializable {
    private static final long serialVersionUID = 5695588849785352130L;
    @ApiModelProperty(value = "评论用户")
    private UserVO commentUser;
    @ApiModelProperty(value = "是否点赞")
    private Boolean isLiked;
}
