package top.ochiamalu.model.request;

import lombok.Data;

import java.util.List;

/**
 * 标签更新请求
 *
 * @author ochiamalu
 * @date 2025/04/15
 */
@Data
public class TagUpdateRequest {
    private Long id;
    private List<String> tags;
}
