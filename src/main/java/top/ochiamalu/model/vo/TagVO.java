package top.ochiamalu.model.vo;

import lombok.Data;
import top.ochiamalu.model.domain.TagChildren;

import java.util.List;

/**
 * 标签vo
 *
 * @author ochiamalu
 * @date 2025/04/15
 */
@Data
public class TagVO {
    /**
     * ID
     */
    private Long id;
    /**
     * 文本
     */
    private String text;
    /**
     * 子标签
     */
    private List<TagChildren> children;
}
