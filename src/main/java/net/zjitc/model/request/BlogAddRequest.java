package net.zjitc.model.request;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
public class BlogAddRequest implements Serializable {
    private static final long serialVersionUID = 8975136896057535409L;

    private MultipartFile[] images;
    private String title;
    private String content;
}
