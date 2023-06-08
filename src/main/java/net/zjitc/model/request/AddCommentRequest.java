package net.zjitc.model.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class AddCommentRequest implements Serializable{
    private static final long serialVersionUID = 5733549433004941655L;
    private Long blogId;
    private String content;

}
