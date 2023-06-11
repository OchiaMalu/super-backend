package net.zjitc.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "添加博文请求")
public class BlogAddRequest implements Serializable {
    private static final long serialVersionUID = 8975136896057535409L;
    @ApiModelProperty(value = "图片")
    private MultipartFile[] images;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "正文")
    private String content;
}
