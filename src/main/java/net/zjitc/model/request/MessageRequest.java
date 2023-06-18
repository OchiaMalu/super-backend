package net.zjitc.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageRequest implements Serializable {
    private static final long serialVersionUID = 1324635911327892058L;
    private Long toId;
    private Long teamId;
    private String text;
    private Integer chatType;
    private boolean isAdmin;
}
