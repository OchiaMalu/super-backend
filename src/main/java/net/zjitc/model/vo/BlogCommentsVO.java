package net.zjitc.model.vo;

import lombok.Data;
import net.zjitc.model.domain.BlogComments;

import java.io.Serializable;
@Data
public class BlogCommentsVO extends BlogComments implements Serializable {
    private static final long serialVersionUID = 5695588849785352130L;
    private UserVO commentUser;
    private Boolean isLiked;
}
