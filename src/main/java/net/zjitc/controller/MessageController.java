package net.zjitc.controller;

import net.zjitc.common.BaseResponse;
import net.zjitc.common.ErrorCode;
import net.zjitc.common.ResultUtils;
import net.zjitc.exception.BusinessException;
import net.zjitc.model.domain.User;
import net.zjitc.model.vo.BlogVO;
import net.zjitc.model.vo.MessageVO;
import net.zjitc.service.MessageService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static net.zjitc.constants.RedisConstants.MESSAGE_BLOG_NUM_KEY;
import static net.zjitc.constants.RedisConstants.MESSAGE_LIKE_NUM_KEY;
import static net.zjitc.constants.UserConstants.USER_LOGIN_STATE;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping
    public BaseResponse<Boolean> userHasNewMessage(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Boolean hasNewMessage = messageService.hasNewMessage(loginUser.getId());
        return ResultUtils.success(hasNewMessage);
    }

    @GetMapping("/num")
    public BaseResponse<Long> getUserMessageNum(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long messageNum = messageService.getMessageNum(loginUser.getId());
        return ResultUtils.success(messageNum);
    }

    @GetMapping("/like/num")
    public BaseResponse<Long> getUserLikeMessageNum(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long messageNum = messageService.getLikeNum(loginUser.getId());
        return ResultUtils.success(messageNum);
    }

    @GetMapping("/like")
    public BaseResponse<List<MessageVO>> getUserLikeMessage(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        List<MessageVO> messageVOList = messageService.getLike(loginUser.getId());
        return ResultUtils.success(messageVOList);
    }

    @GetMapping("/blog/num")
    public BaseResponse<String> getUserBlogMessageNum(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        String likeNumKey = MESSAGE_BLOG_NUM_KEY + loginUser.getId();
        Boolean hasKey = stringRedisTemplate.hasKey(likeNumKey);
        if (Boolean.TRUE.equals(hasKey)) {
            String num = stringRedisTemplate.opsForValue().get(likeNumKey);
            return ResultUtils.success(num);
        } else {
            return ResultUtils.success("0");
        }
    }

    @GetMapping("/blog")
    public BaseResponse<List<BlogVO>> getUserBlogMessage(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        List<BlogVO> blogVOList = messageService.getUserBlog(loginUser.getId());
        return ResultUtils.success(blogVOList);
    }
}
