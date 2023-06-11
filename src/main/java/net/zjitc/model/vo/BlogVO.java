package net.zjitc.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.zjitc.model.domain.Blog;

import java.io.Serializable;

@Data
@ApiModel(value = "博文返回")
public class BlogVO extends Blog implements Serializable {
    private static final long serialVersionUID = -1461567317259590205L;
    @ApiModelProperty(value = "是否点赞")
    private Boolean isLike;
    @ApiModelProperty(value = "封面图片")
    private String coverImage;
    @ApiModelProperty(value = "作者")
    private UserVO author;
}
