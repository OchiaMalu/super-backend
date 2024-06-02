package net.zjitc.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * ai 请求
 *
 * @author OchiaMalu
 * @date 2024/06/01
 */
@Data
public class AIRequest implements Serializable {
    private static final long serialVersionUID = 8360772908962851969L;

    /**
     * 用户消息
     */
    private String message;
}
