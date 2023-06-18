package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.common.ErrorCode;
import net.zjitc.exception.BusinessException;
import net.zjitc.model.domain.Friends;
import net.zjitc.model.domain.User;
import net.zjitc.model.request.FriendAddRequest;
import net.zjitc.service.FriendsService;
import net.zjitc.mapper.FriendsMapper;
import net.zjitc.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static net.zjitc.constants.FriendConstant.DEFAULT_STATUS;
import static net.zjitc.constants.RedissonConstant.APPLY_LOCK;

/**
 * @author OchiaMalu
 * @description 针对表【friends(好友申请管理表)】的数据库操作Service实现
 * @createDate 2023-06-18 14:10:45
 */
@Service
public class FriendsServiceImpl extends ServiceImpl<FriendsMapper, Friends>
        implements FriendsService {

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public boolean addFriendRecords(User loginUser, FriendAddRequest friendAddRequest) {
        if (StringUtils.isNotBlank(friendAddRequest.getRemark()) && friendAddRequest.getRemark().length() > 120) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "申请备注最多120个字符");
        }
        if (ObjectUtils.anyNull(loginUser.getId(), friendAddRequest.getReceiveId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "添加失败");
        }
        // 1.添加的不能是自己
        if (Objects.equals(loginUser.getId(), friendAddRequest.getReceiveId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能添加自己为好友");
        }
        RLock lock = redissonClient.getLock(APPLY_LOCK + loginUser.getId());
        try {
            // 抢到锁并执行
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                // 2.条数大于等于1 就不能再添加
                LambdaQueryWrapper<Friends> friendsLambdaQueryWrapper = new LambdaQueryWrapper<>();
                friendsLambdaQueryWrapper.eq(Friends::getReceiveId, friendAddRequest.getReceiveId());
                friendsLambdaQueryWrapper.eq(Friends::getFromId, loginUser.getId());
                List<Friends> list = this.list(friendsLambdaQueryWrapper);
                list.forEach(friends -> {
                    if (list.size() > 1 && friends.getStatus() == DEFAULT_STATUS) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能重复申请");
                    }
                });
                Friends newFriend = new Friends();
                newFriend.setFromId(loginUser.getId());
                newFriend.setReceiveId(friendAddRequest.getReceiveId());
                if (StringUtils.isBlank(friendAddRequest.getRemark())) {
                    newFriend.setRemark("我是" + userService.getById(loginUser.getId()).getUsername());
                } else {
                    newFriend.setRemark(friendAddRequest.getRemark());
                }
                newFriend.setCreateTime(new Date());
                return this.save(newFriend);
            }
        } catch (InterruptedException e) {
            log.error("joinTeam error", e);
            return false;
        } finally {
            // 只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
        return false;
    }
}




