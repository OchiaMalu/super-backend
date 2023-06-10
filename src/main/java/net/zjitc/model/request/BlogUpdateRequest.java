package net.zjitc.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class BlogUpdateRequest implements Serializable {
    private static final long serialVersionUID = -669161052567797556L;
    private Long id;
    private String imgStr;
    private MultipartFile[] images;
    private String title;
    private String content;
}
