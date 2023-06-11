package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.model.domain.Follow;
import net.zjitc.service.FollowService;
import net.zjitc.mapper.FollowMapper;
import org.springframework.stereotype.Service;

/**
 * @author OchiaMalu
 * @description 针对表【follow】的数据库操作Service实现
 * @createDate 2023-06-11 13:02:31
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
        implements FollowService {

    @Override
    public void followUser(Long followUserId, Long userId) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFollowUserId, followUserId).eq(Follow::getUserId, userId);
        long count = this.count(followLambdaQueryWrapper);
        if (count == 0) {
            Follow follow = new Follow();
            follow.setFollowUserId(followUserId);
            follow.setUserId(userId);
            this.save(follow);
        } else {
            this.remove(followLambdaQueryWrapper);
        }
    }
}




