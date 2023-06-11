package net.zjitc.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel(value = "添加博文评论请求")
public class AddCommentRequest implements Serializable{
    private static final long serialVersionUID = 5733549433004941655L;
    @ApiModelProperty(value = "博文id")
    private Long blogId;
    @ApiModelProperty(value = "评论")
    private String content;

}
