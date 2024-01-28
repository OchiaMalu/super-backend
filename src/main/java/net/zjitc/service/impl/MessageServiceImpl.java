package net.zjitc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.common.ErrorCode;
import net.zjitc.exception.BusinessException;
import net.zjitc.mapper.MessageMapper;
import net.zjitc.model.domain.Message;
import net.zjitc.model.domain.User;
import net.zjitc.model.enums.MessageTypeEnum;
import net.zjitc.model.vo.BlogCommentsVO;
import net.zjitc.model.vo.BlogVO;
import net.zjitc.model.vo.MessageVO;
import net.zjitc.model.vo.UserVO;
import net.zjitc.service.BlogCommentsService;
import net.zjitc.service.BlogService;
import net.zjitc.service.MessageService;
import net.zjitc.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static net.zjitc.constants.RedisConstants.BLOG_FEED_KEY;
import static net.zjitc.constants.RedisConstants.MESSAGE_BLOG_NUM_KEY;
import static net.zjitc.constants.RedisConstants.MESSAGE_LIKE_NUM_KEY;
import static net.zjitc.constants.SystemConstants.PAGE_SIZE;

/**
 * @author OchiaMalu
 * @description 针对表【message】的数据库操作Service实现
 * @createDate 2023-06-21 17:39:30
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService {

    @Resource
    @Lazy
    private BlogCommentsService blogCommentsService;

    @Resource
    @Lazy
    private BlogService blogService;

    @Resource
    @Lazy
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public long getMessageNum(Long userId) {
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        messageLambdaQueryWrapper.eq(Message::getToId, userId).eq(Message::getIsRead, 0);
        return this.count(messageLambdaQueryWrapper);
    }

    @Override
    public long getLikeNum(Long userId) {
        String likeNumKey = MESSAGE_LIKE_NUM_KEY + userId;
        Boolean hasLike = stringRedisTemplate.hasKey(likeNumKey);
        if (Boolean.TRUE.equals(hasLike)) {
            String likeNum = stringRedisTemplate.opsForValue().get(likeNumKey);
            assert likeNum != null;
            return Long.parseLong(likeNum);
        } else {
            return 0;
        }
    }

    @Override
    public List<MessageVO> getLike(Long userId) {
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        messageLambdaQueryWrapper.eq(Message::getToId, userId)
                .and(wp -> wp.eq(Message::getType, 0).or().eq(Message::getType, 1))
                .orderBy(true, false, Message::getCreateTime);
        List<Message> messageList = this.list(messageLambdaQueryWrapper);
        if (messageList.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaUpdateWrapper<Message> messageLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        messageLambdaUpdateWrapper.eq(Message::getToId, userId).eq(Message::getType, 0)
                .or().eq(Message::getType, 1).set(Message::getIsRead, 1);
        this.update(messageLambdaUpdateWrapper);
        String likeNumKey = MESSAGE_LIKE_NUM_KEY + userId;
        Boolean hasLike = stringRedisTemplate.hasKey(likeNumKey);
        if (Boolean.TRUE.equals(hasLike)) {
            stringRedisTemplate.opsForValue().set(likeNumKey, "0");
        }
        return messageList.stream().map((item) -> {
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(item, messageVO);
            User user = userService.getById(messageVO.getFromId());
            if (user == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "发送人不存在");
            }
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            messageVO.setFromUser(userVO);
            if (item.getType() == MessageTypeEnum.BLOG_COMMENT_LIKE.getValue()) {
                BlogCommentsVO commentsVO = blogCommentsService.getComment(Long.parseLong(item.getData()), userId);
                messageVO.setComment(commentsVO);
            }
            if (item.getType() == MessageTypeEnum.BLOG_LIKE.getValue()) {
                BlogVO blogVO = blogService.getBlogById(Long.parseLong(item.getData()), userId);
                messageVO.setBlog(blogVO);
            }
            return messageVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<BlogVO> getUserBlog(Long userId) {
        String key = BLOG_FEED_KEY + userId;
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(key, 0, System.currentTimeMillis(), 0, PAGE_SIZE);
        if (typedTuples == null || typedTuples.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<BlogVO> blogVOList = new ArrayList<>(typedTuples.size());
        for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
            long blogId = Long.parseLong(Objects.requireNonNull(tuple.getValue()));
            BlogVO blogVO = blogService.getBlogById(blogId, userId);
            blogVOList.add(blogVO);
        }
        String likeNumKey = MESSAGE_BLOG_NUM_KEY + userId;
        Boolean hasKey = stringRedisTemplate.hasKey(likeNumKey);
        if (Boolean.TRUE.equals(hasKey)) {
            stringRedisTemplate.opsForValue().set(likeNumKey, "0");
        }
        return blogVOList;
    }

    @Override
    public Boolean hasNewMessage(Long userId) {
        String likeNumKey = MESSAGE_LIKE_NUM_KEY + userId;
        Boolean hasLike = stringRedisTemplate.hasKey(likeNumKey);
        if (Boolean.TRUE.equals(hasLike)) {
            String likeNum = stringRedisTemplate.opsForValue().get(likeNumKey);
            assert likeNum != null;
            if (Long.parseLong(likeNum) > 0) {
                return true;
            }
        }
        String blogNumKey = MESSAGE_BLOG_NUM_KEY + userId;
        Boolean hasBlog = stringRedisTemplate.hasKey(blogNumKey);
        if (Boolean.TRUE.equals(hasBlog)) {
            String blogNum = stringRedisTemplate.opsForValue().get(blogNumKey);
            assert blogNum != null;
            return Long.parseLong(blogNum) > 0;
        }
        return false;
    }

    @Override
    public Page<MessageVO> pageLike(Long userId, Long currentPage) {
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        messageLambdaQueryWrapper.eq(Message::getToId, userId)
                .and(wp -> wp.eq(Message::getType, 0).or().eq(Message::getType, 1))
                .orderBy(true, false, Message::getCreateTime);
        Page<Message> messagePage = this.page(new Page<>(currentPage, PAGE_SIZE), messageLambdaQueryWrapper);
        if (messagePage.getSize() == 0) {
            return new Page<>();
        }
        Page<MessageVO> messageVoPage = new Page<>();
        BeanUtils.copyProperties(messagePage, messageVoPage);
        LambdaUpdateWrapper<Message> messageLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        messageLambdaUpdateWrapper.eq(Message::getToId, userId).eq(Message::getType, 0)
                .or().eq(Message::getType, 1).set(Message::getIsRead, 1);
        this.update(messageLambdaUpdateWrapper);
        String likeNumKey = MESSAGE_LIKE_NUM_KEY + userId;
        Boolean hasLike = stringRedisTemplate.hasKey(likeNumKey);
        if (Boolean.TRUE.equals(hasLike)) {
            stringRedisTemplate.opsForValue().set(likeNumKey, "0");
        }
        List<MessageVO> messageVOList = messagePage.getRecords().stream().map((item) -> {
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(item, messageVO);
            User user = userService.getById(messageVO.getFromId());
            if (user == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "发送人不存在");
            }
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            messageVO.setFromUser(userVO);
            if (item.getType() == MessageTypeEnum.BLOG_COMMENT_LIKE.getValue()) {
                BlogCommentsVO commentsVO = blogCommentsService.getComment(Long.parseLong(item.getData()), userId);
                if (commentsVO == null) {
                    BlogCommentsVO tempCommon = new BlogCommentsVO();
                    tempCommon.setContent("该评论已被删除");
                    messageVO.setComment(tempCommon);
                } else {
                    messageVO.setComment(commentsVO);
                }
            }
            if (item.getType() == MessageTypeEnum.BLOG_LIKE.getValue()) {
                BlogVO blogVO = blogService.getBlogById(Long.parseLong(item.getData()), userId);
                messageVO.setBlog(blogVO);
            }
            return messageVO;
        }).collect(Collectors.toList());
        messageVoPage.setRecords(messageVOList);
        return messageVoPage;
    }
}




