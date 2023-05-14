package net.zjitc.model.enums;

public enum TeamStatusEnum {
    PUBLIC(0, "公开"),
    PRIVATE(1, "私有"),
    SECRET(2, "加密");
    private int value;

    private String text;

    public static TeamStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum teamStatusEnum : values) {
            if (teamStatusEnum.getValue() == value) {
                return teamStatusEnum;
            }
        }
        return null;
    }

    TeamStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public TeamStatusEnum setValue(int value) {
        this.value = value;
        return this;
    }

    public String getText() {
        return text;
    }

    public TeamStatusEnum setText(String text) {
        this.text = text;
        return this;
    }
}
