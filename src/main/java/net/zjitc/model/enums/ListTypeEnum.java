package net.zjitc.model.enums;

/**
 * 枚举类型列表
 *
 * @author OchiaMalu
 * @date 2023/07/26
 */
public enum ListTypeEnum {
    
    MIX(0, "中英文混合"),
    CHINESE(1, "纯中文"),
    ENGLISH(2, "纯英文"),
    ENGLISH_AND_OTHER(3, "英文和其他字符");
    /**
     * 价值
     */
    private int value;

    /**
     * 文本
     */
    private String text;

    /**
     * 得到枚举值
     *
     * @param value 价值
     * @return {@link ListTypeEnum}
     */
    public static ListTypeEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        //values():返回所有枚举类对象构成的数组
        ListTypeEnum[] values = ListTypeEnum.values();
        //遍历这个数组
        for (ListTypeEnum teamStatusEnum : values) {
            if (teamStatusEnum.getValue() == value) {
                return teamStatusEnum;
            }
        }
        return null;
    }

    /**
     * 枚举类型列表
     *
     * @param value 价值
     * @param text  文本
     */
    ListTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 获得价值
     *
     * @return int
     */
    public int getValue() {
        return value;
    }

    /**
     * 设置值
     *
     * @param value 价值
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * 得到文本
     *
     * @return {@link String}
     */
    public String getText() {
        return text;
    }

    /**
     * 设置文本
     *
     * @param text 文本
     */
    public void setText(String text) {
        this.text = text;
    }
}
