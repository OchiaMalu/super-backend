package net.zjitc.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.zjitc.common.ErrorCode;
import net.zjitc.exception.BusinessException;
import net.zjitc.model.domain.Chat;
import net.zjitc.model.domain.User;
import net.zjitc.model.request.ChatRequest;
import net.zjitc.model.vo.MessageVO;
import net.zjitc.model.vo.WebSocketVO;
import net.zjitc.service.ChatService;
import net.zjitc.mapper.ChatMapper;
import net.zjitc.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static net.zjitc.constants.ChatConstant.CACHE_CHAT_HALL;
import static net.zjitc.constants.ChatConstant.CACHE_CHAT_PRIVATE;

/**
* @author OchiaMalu
* @description 针对表【chat(聊天消息表)】的数据库操作Service实现
* @createDate 2023-06-17 21:50:15
*/
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat>
    implements ChatService{

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    @Override
    public List<MessageVO> getPrivateChat(ChatRequest chatRequest, int chatType, User loginUser) {
        Long toId = chatRequest.getToId();
        if (toId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<MessageVO> chatRecords = getCache(CACHE_CHAT_PRIVATE, loginUser.getId() + "" + toId);
        if (chatRecords != null) {
            saveCache(CACHE_CHAT_PRIVATE, loginUser.getId() + "" + toId, chatRecords);
            return chatRecords;
        }
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.
                and(privateChat -> privateChat.eq(Chat::getFromId, loginUser.getId()).eq(Chat::getToid, toId)
                        .or().
                        eq(Chat::getToid, loginUser.getId()).eq(Chat::getFromId, toId)
                ).eq(Chat::getChatType, chatType);
        // 两方共有聊天
        List<Chat> list = this.list(chatLambdaQueryWrapper);
        List<MessageVO> messageVoList = list.stream().map(chat -> {
            MessageVO messageVo = chatResult(loginUser.getId(), toId, chat.getText(), chatType, chat.getCreateTime());
            if (chat.getFromId().equals(loginUser.getId())) {
                messageVo.setIsMy(true);
            }
            return messageVo;
        }).collect(Collectors.toList());
        saveCache(CACHE_CHAT_PRIVATE, loginUser.getId() + "" + toId, messageVoList);
        return messageVoList;
    }

    @Override
    public List<MessageVO> getCache(String redisKey, String id) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        List<MessageVO> chatRecords;
        if (redisKey.equals(CACHE_CHAT_HALL)) {
            chatRecords = (List<MessageVO>) valueOperations.get(redisKey);
        } else {
            chatRecords = (List<MessageVO>) valueOperations.get(redisKey + id);
        }
        return chatRecords;
    }

    @Override
    public void saveCache(String redisKey, String id, List<MessageVO> messageVos) {
        try {
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            // 解决缓存雪崩
            int i = RandomUtil.randomInt(2, 3);
            if (redisKey.equals(CACHE_CHAT_HALL)) {
                valueOperations.set(redisKey, messageVos, 2 + i / 10, TimeUnit.MINUTES);
            } else {
                valueOperations.set(redisKey + id, messageVos, 2 + i / 10, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            log.error("redis set key error");
        }
    }
    @Override
    public MessageVO chatResult(Long userId, Long toId, String text, Integer chatType, Date createTime) {
        MessageVO messageVo = new MessageVO();
        User fromUser = userService.getById(userId);
        User toUser = userService.getById(toId);
        WebSocketVO fromWebSocketVo = new WebSocketVO();
        WebSocketVO toWebSocketVo = new WebSocketVO();
        BeanUtils.copyProperties(fromUser, fromWebSocketVo);
        BeanUtils.copyProperties(toUser, toWebSocketVo);
        messageVo.setFormUser(fromWebSocketVo);
        messageVo.setToUser(toWebSocketVo);
        messageVo.setChatType(chatType);
        messageVo.setText(text);
        messageVo.setCreateTime(DateUtil.format(createTime, "yyyy-MM-dd HH:mm:ss"));
        return messageVo;
    }

    @Override
    public void deleteKey(String key, String id) {
        if (key.equals(CACHE_CHAT_HALL)) {
            redisTemplate.delete(key);
        } else {
            redisTemplate.delete(key + id);
        }
    }
}




