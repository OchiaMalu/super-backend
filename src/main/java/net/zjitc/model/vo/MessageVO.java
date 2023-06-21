package net.zjitc.model.vo;

import lombok.Data;
import net.zjitc.model.domain.Message;

@Data
public class MessageVO extends Message {
    private static final long serialVersionUID = 4353136955942044222L;
    private UserVO fromUser;
    private BlogVO blog;
    private BlogCommentsVO comment;
}
