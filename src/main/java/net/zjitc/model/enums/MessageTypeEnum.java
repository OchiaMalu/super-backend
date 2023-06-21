package net.zjitc.model.enums;

public enum MessageTypeEnum {
    BLOG_LIKE(0,"博文点赞"),
    BLOG_COMMENT_LIKE(1,"博文评论点赞");

    private int value;
    private String text;

    MessageTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public MessageTypeEnum setValue(int value) {
        this.value = value;
        return this;
    }

    public String getText() {
        return text;
    }

    public MessageTypeEnum setText(String text) {
        this.text = text;
        return this;
    }
}
