package net.zjitc.model.vo;

import lombok.Data;
import net.zjitc.model.domain.Blog;
import net.zjitc.model.domain.User;

import java.io.Serializable;

@Data
public class BlogVO extends Blog implements Serializable {
    private static final long serialVersionUID = -1461567317259590205L;
    private Boolean isLike;
    private String coverImage;
    private UserVO author;
}
