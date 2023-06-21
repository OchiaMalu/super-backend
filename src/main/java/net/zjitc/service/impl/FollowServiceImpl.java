package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.model.domain.Follow;
import net.zjitc.model.domain.User;
import net.zjitc.model.vo.UserVO;
import net.zjitc.service.FollowService;
import net.zjitc.mapper.FollowMapper;
import net.zjitc.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author OchiaMalu
 * @description 针对表【follow】的数据库操作Service实现
 * @createDate 2023-06-11 13:02:31
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
        implements FollowService {

    @Resource
    @Lazy
    private UserService userService;

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

    @Override
    public List<User> listFans(Long userId) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFollowUserId,userId);
        List<Follow> list = this.list(followLambdaQueryWrapper);
        return list.stream().map((follow -> userService.getById(follow.getUserId()))).collect(Collectors.toList());
    }

    @Override
    public List<UserVO> listMyFollow(Long userId) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getUserId,userId);
        List<Follow> list = this.list(followLambdaQueryWrapper);
        List<User> userList = list.stream().map((follow -> userService.getById(follow.getFollowUserId()))).collect(Collectors.toList());
        return userList.stream().map((user) -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setIsFollow(true);
            return userVO;
        }).collect(Collectors.toList());
    }
}




