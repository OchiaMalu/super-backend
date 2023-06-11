package net.zjitc.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@ApiModel(value = "更新博文请求")
public class BlogUpdateRequest implements Serializable {
    private static final long serialVersionUID = -669161052567797556L;
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "已上传的图片")
    private String imgStr;
    @ApiModelProperty(value = "未上传的图片")
    private MultipartFile[] images;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "正文")
    private String content;
}
