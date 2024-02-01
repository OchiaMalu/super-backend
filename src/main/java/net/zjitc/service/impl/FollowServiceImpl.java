package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.mapper.FollowMapper;
import net.zjitc.model.domain.Follow;
import net.zjitc.model.domain.User;
import net.zjitc.model.vo.UserVO;
import net.zjitc.service.FollowService;
import net.zjitc.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.zjitc.constants.SystemConstants.PAGE_SIZE;

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
    public List<UserVO> listFans(Long userId) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFollowUserId, userId);
        List<Follow> list = this.list(followLambdaQueryWrapper);
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<User> userList = list.stream().map((follow -> userService.getById(follow.getUserId())))
                .filter(Objects::nonNull).collect(Collectors.toList());
        return userList.stream().map((user) -> this.getUserFollowInfo(user, userId)).collect(Collectors.toList());
    }


    @Override
    public List<UserVO> listMyFollow(Long userId) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getUserId, userId);
        List<Follow> list = this.list(followLambdaQueryWrapper);
        List<User> userList = list.stream().map((follow -> userService.getById(follow.getFollowUserId())))
                .collect(Collectors.toList());
        return userList.stream().map((user) -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setIsFollow(true);
            return userVO;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<UserVO> pageMyFollow(Long userId, String currentPage) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getUserId, userId);
        Page<Follow> followPage = this.page(
                new Page<>(Long.parseLong(currentPage), PAGE_SIZE),
                followLambdaQueryWrapper);
        if (followPage == null || followPage.getSize() == 0) {
            return new Page<>();
        }
        Page<UserVO> userVoPage = new Page<>();
        List<User> userList = followPage.getRecords().stream()
                .map((follow -> userService.getById(follow.getFollowUserId())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<UserVO> userVOList = userList.stream().map((user) -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setIsFollow(true);
            return userVO;
        }).collect(Collectors.toList());
        return userVoPage.setRecords(userVOList);
    }

    @Override
    public Page<UserVO> pageFans(Long userId, String currentPage) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFollowUserId, userId);
        Page<Follow> followPage = this.page(
                new Page<>(Long.parseLong(currentPage),
                        PAGE_SIZE),
                followLambdaQueryWrapper);
        if (followPage == null || followPage.getSize() == 0) {
            return new Page<>();
        }
        Page<UserVO> userVoPage = new Page<>();
        BeanUtils.copyProperties(followPage, userVoPage);
        List<User> userList = followPage.getRecords().stream()
                .map((follow -> userService.getById(follow.getUserId())))
                .filter(Objects::nonNull).collect(Collectors.toList());
        List<UserVO> userVOList = userList.stream()
                .map((user) -> this.getUserFollowInfo(user, userId))
                .collect(Collectors.toList());
        userVoPage.setRecords(userVOList);
        return userVoPage;
    }

    @Override
    public UserVO getUserFollowInfo(User user, long userId) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper
                .eq(Follow::getUserId, userId)
                .eq(Follow::getFollowUserId, userVO.getId());
        long count = this.count(followLambdaQueryWrapper);
        userVO.setIsFollow(count > 0);
        return userVO;
    }
}




