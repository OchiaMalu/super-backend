package net.zjitc.model.request;

import lombok.Data;

/**
 * 团队退出请求
 *
 * @author OchiaMalu
 * @date 2024/01/25
 */
@Data
public class TeamKickOutRequest {
    private Long teamId;
    private Long userId;
}
