package net.zjitc.service;

import net.zjitc.model.domain.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author OchiaMalu
* @description 针对表【follow】的数据库操作Service
* @createDate 2023-06-11 13:02:31
*/
public interface FollowService extends IService<Follow> {

    void followUser(Long followUserId, Long userId);
}
