package net.zjitc.jobs;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.util.Pair;
import net.zjitc.model.domain.Follow;
import net.zjitc.model.domain.User;
import net.zjitc.model.vo.UserVO;
import net.zjitc.service.FollowService;
import net.zjitc.service.UserService;
import net.zjitc.utils.AlgorithmUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static net.zjitc.constants.RedisConstants.USER_RECOMMEND_KEY;
import static net.zjitc.constants.RedissonConstant.USER_RECOMMEND_LOCK;
import static net.zjitc.constants.SystemConstants.DEFAULT_CACHE_PAGE;
import static net.zjitc.constants.SystemConstants.PAGE_SIZE;

public class UserRecommendationCache extends QuartzJobBean {
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private FollowService followService;

    private List<User> userList = new ArrayList<>();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        RLock lock = redissonClient.getLock(USER_RECOMMEND_LOCK);
        try {
            if (lock.tryLock(0, -1, TimeUnit.MICROSECONDS)) {
                System.out.println("开始用户缓存");
                long begin = System.currentTimeMillis();
                userList = userService.list();
                for (User user : userList) {
                    for (int i = 1; i <= DEFAULT_CACHE_PAGE; i++) {
                        Page<UserVO> userVOPage = this.matchUser(i, user);
                        Gson gson = new Gson();
                        String userVOPageStr = gson.toJson(userVOPage);
                        String key = USER_RECOMMEND_KEY + user.getId() + ":" + i;
                        stringRedisTemplate.opsForValue().set(key, userVOPageStr);
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println("用户缓存结束，耗时" + (end - begin));
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }

    }

    private Page<UserVO> matchUser(long currentPage, User loginUser) throws IOException {
        String tags = loginUser.getTags();
        if (tags == null) {
            return userService.userPage(currentPage);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tags");
        queryWrapper.isNotNull("tags");
        Gson gson = new Gson();
        List<String> tagList = gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
        // 用户列表的下标 => 相似度
        List<Pair<User, Long>> list = new ArrayList<>();
        // 依次计算所有用户和当前用户的相似度
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            String userTags = user.getTags();
            // 无标签或者为当前用户自己
            if (StringUtils.isBlank(userTags) || Objects.equals(user.getId(), loginUser.getId())) {
                continue;
            }
            List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<String>>() {
            }.getType());
            // 计算分数
            long distance = (long) AlgorithmUtil.minDistance(tagList, userTagList);
            list.add(new Pair<>(user, distance));
        }
        // 按编辑距离由小到大排序
        List<Pair<User, Long>> topUserPairList = list.stream()
                .sorted((a, b) -> (int) (a.getValue() - b.getValue()))
                .collect(Collectors.toList());
        //截取currentPage所需的List
        ArrayList<Pair<User, Long>> finalUserPairList = new ArrayList<>();
        int begin = (int) ((currentPage - 1) * PAGE_SIZE);
        int end = (int) (((currentPage - 1) * PAGE_SIZE) + PAGE_SIZE) - 1;
        if (topUserPairList.size() < end) {
            //剩余数量
            int temp = (int) (topUserPairList.size() - begin);
            if (temp <= 0) {
                return new Page<>();
            }
            for (int i = begin; i <= begin + temp - 1; i++) {
                finalUserPairList.add(topUserPairList.get(i));
            }
        } else {
            for (int i = begin; i < end; i++) {
                finalUserPairList.add(topUserPairList.get(i));
            }
        }
        //获取排列后的UserId
        List<Long> userIdList = finalUserPairList.stream().map(pair -> pair.getKey().getId()).collect(Collectors.toList());
        String idStr = StringUtils.join(userIdList, ",");
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userIdList).last("ORDER BY FIELD(id," + idStr + ")");
        List<UserVO> userVOList = userService.list(userQueryWrapper)
                .stream()
                .map((user) -> {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(user, userVO);
                    LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    followLambdaQueryWrapper.eq(Follow::getUserId, loginUser.getId()).eq(Follow::getFollowUserId, userVO.getId());
                    long count = followService.count(followLambdaQueryWrapper);
                    userVO.setIsFollow(count > 0);
                    return userVO;
                })
                .collect(Collectors.toList());
        Page<UserVO> userVOPage = new Page<>();
        userVOPage.setRecords(userVOList);
        userVOPage.setCurrent(currentPage);
        userVOPage.setSize(userVOList.size());
        userVOPage.setTotal(userVOList.size());
        return userVOPage;
    }
}
