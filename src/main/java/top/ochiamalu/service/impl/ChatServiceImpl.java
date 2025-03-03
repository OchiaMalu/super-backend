package top.ochiamalu.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.ochiamalu.common.ErrorCode;
import top.ochiamalu.exception.BusinessException;
import top.ochiamalu.mapper.ChatMapper;
import top.ochiamalu.model.domain.Chat;
import top.ochiamalu.model.domain.Team;
import top.ochiamalu.model.domain.User;
import top.ochiamalu.model.request.ChatRequest;
import top.ochiamalu.model.vo.ChatMessageVO;
import top.ochiamalu.model.vo.PrivateChatVO;
import top.ochiamalu.model.vo.UserVO;
import top.ochiamalu.model.vo.WebSocketVO;
import top.ochiamalu.service.ChatService;
import top.ochiamalu.service.TeamService;
import top.ochiamalu.service.UserService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static top.ochiamalu.constants.ChatConstant.CACHE_CHAT_HALL;
import static top.ochiamalu.constants.ChatConstant.CACHE_CHAT_PRIVATE;
import static top.ochiamalu.constants.ChatConstant.CACHE_CHAT_TEAM;
import static top.ochiamalu.constants.ChatConstant.PRIVATE_CHAT;
import static top.ochiamalu.constants.RedisConstants.CACHE_TIME_OFFSET;
import static top.ochiamalu.constants.RedisConstants.MAXIMUM_CACHE_RANDOM_TIME;
import static top.ochiamalu.constants.RedisConstants.MINIMUM_CACHE_RANDOM_TIME;
import static top.ochiamalu.constants.UserConstants.ADMIN_ROLE;

/**
 * 聊天服务实现
 *
 * @author OchiaMalu
 * @description 针对表【chat(聊天消息表)】的数据库操作Service实现
 * @createDate 2023-06-17 21:50:15
 * @date 2024/01/25
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat>
        implements ChatService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    /**
     * 获取私人聊天
     *
     * @param chatRequest 聊天请求
     * @param chatType    聊天类型
     * @param loginUser   登录用户
     * @return {@link List}<{@link ChatMessageVO}>
     */
    @Override
    public List<ChatMessageVO> getPrivateChat(ChatRequest chatRequest, int chatType, User loginUser) {
        Long toId = chatRequest.getToId();
        if (toId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<ChatMessageVO> chatRecords = getCache(CACHE_CHAT_PRIVATE, loginUser.getId() + String.valueOf(toId));
        if (chatRecords != null) {
            saveCache(CACHE_CHAT_PRIVATE, loginUser.getId() + String.valueOf(toId), chatRecords);
            return chatRecords;
        }
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.
                and(privateChat -> privateChat.eq(Chat::getFromId, loginUser.getId()).eq(Chat::getToId, toId)
                        .or().
                        eq(Chat::getToId, loginUser.getId()).eq(Chat::getFromId, toId)
                ).eq(Chat::getChatType, chatType);
        // 两方共有聊天
        List<Chat> list = this.list(chatLambdaQueryWrapper);
        List<ChatMessageVO> chatMessageVOList = list.stream().map(chat -> {
            ChatMessageVO chatMessageVo = chatResult(loginUser.getId(),
                    toId, chat.getText(), chatType, chat.getMessageType(),
                    chat.getCreateTime());
            if (chat.getFromId().equals(loginUser.getId())) {
                chatMessageVo.setIsMy(true);
            }
            return chatMessageVo;
        }).collect(Collectors.toList());
        saveCache(CACHE_CHAT_PRIVATE, loginUser.getId() + String.valueOf(toId), chatMessageVOList);
        return chatMessageVOList;
    }

    /**
     * 获取缓存
     *
     * @param redisKey redis键
     * @param id       id
     * @return {@link List}<{@link ChatMessageVO}>
     */
    @Override
    public List<ChatMessageVO> getCache(String redisKey, String id) {
        List<ChatMessageVO> chatRecords;
        Gson gson = new Gson();
        String messageJSONStr;
        if (redisKey.equals(CACHE_CHAT_HALL)) {
            messageJSONStr = stringRedisTemplate.opsForValue().get(redisKey);
        } else {
            messageJSONStr = stringRedisTemplate.opsForValue().get(redisKey + id);
        }
        chatRecords = gson.fromJson(messageJSONStr, new TypeToken<List<ChatMessageVO>>() {
        }.getType());
        return chatRecords;
    }

    /**
     * 保存缓存
     *
     * @param redisKey       redis键
     * @param id             id
     * @param chatMessageVOS 聊天消息vos
     */
    @Override
    public void saveCache(String redisKey, String id, List<ChatMessageVO> chatMessageVOS) {
        try {
            Gson gson = new Gson();
            String messageJSONStr = gson.toJson(chatMessageVOS);
            // 解决缓存雪崩
            int i = RandomUtil.randomInt(MINIMUM_CACHE_RANDOM_TIME, MAXIMUM_CACHE_RANDOM_TIME);
            if (redisKey.equals(CACHE_CHAT_HALL)) {
                stringRedisTemplate.opsForValue().set(
                        redisKey,
                        messageJSONStr,
                        MINIMUM_CACHE_RANDOM_TIME + i / CACHE_TIME_OFFSET,
                        TimeUnit.MINUTES);
            } else {
                stringRedisTemplate.opsForValue().set(
                        redisKey + id,
                        messageJSONStr,
                        MINIMUM_CACHE_RANDOM_TIME + i / CACHE_TIME_OFFSET,
                        TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            log.error("redis set key error");
        }
    }

    /**
     * 聊天结果
     *
     * @param userId 用户id
     * @param text   文本
     * @return {@link ChatMessageVO}
     */
    private ChatMessageVO chatResult(Long userId, String text, String messageType) {
        ChatMessageVO chatMessageVo = new ChatMessageVO();
        User fromUser = userService.getById(userId);
        WebSocketVO fromWebSocketVo = new WebSocketVO();
        BeanUtils.copyProperties(fromUser, fromWebSocketVo);
        chatMessageVo.setFromUser(fromWebSocketVo);
        chatMessageVo.setText(text);
        chatMessageVo.setMessageType(messageType);
        return chatMessageVo;
    }

    /**
     * 聊天结果
     *
     * @param userId     用户id
     * @param toId       到id
     * @param text       文本
     * @param chatType   聊天类型
     * @param createTime 创建时间
     * @return {@link ChatMessageVO}
     */
    @Override
    public ChatMessageVO chatResult(Long userId, Long toId, String text, Integer chatType, String messageType, Date createTime) {
        ChatMessageVO chatMessageVo = new ChatMessageVO();
        User fromUser = userService.getById(userId);
        User toUser = userService.getById(toId);
        WebSocketVO fromWebSocketVo = new WebSocketVO();
        WebSocketVO toWebSocketVo = new WebSocketVO();
        BeanUtils.copyProperties(fromUser, fromWebSocketVo);
        BeanUtils.copyProperties(toUser, toWebSocketVo);
        chatMessageVo.setFromUser(fromWebSocketVo);
        chatMessageVo.setToUser(toWebSocketVo);
        chatMessageVo.setChatType(chatType);
        chatMessageVo.setMessageType(messageType);
        chatMessageVo.setText(text);
        chatMessageVo.setCreateTime(DateUtil.format(createTime, "yyyy-MM-dd HH:mm:ss"));
        return chatMessageVo;
    }

    /**
     * 删除密钥
     *
     * @param key 钥匙
     * @param id  id
     */
    @Override
    public void deleteKey(String key, String id) {
        if (key.equals(CACHE_CHAT_HALL)) {
            stringRedisTemplate.delete(key);
        } else {
            stringRedisTemplate.delete(key + id);
        }
    }

    /**
     * 获取团队聊天
     *
     * @param chatRequest 聊天请求
     * @param chatType    聊天类型
     * @param loginUser   登录用户
     * @return {@link List}<{@link ChatMessageVO}>
     */
    @Override
    public List<ChatMessageVO> getTeamChat(ChatRequest chatRequest, int chatType, User loginUser) {
        Long teamId = chatRequest.getTeamId();
        if (teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求有误");
        }
        List<ChatMessageVO> chatRecords = getCache(CACHE_CHAT_TEAM, String.valueOf(teamId));
        if (chatRecords != null) {
            List<ChatMessageVO> chatMessageVOS = checkIsMyMessage(loginUser, chatRecords);
            saveCache(CACHE_CHAT_TEAM, String.valueOf(teamId), chatMessageVOS);
            return chatMessageVOS;
        }
        Team team = teamService.getById(teamId);
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.eq(Chat::getChatType, chatType).eq(Chat::getTeamId, teamId);
        List<ChatMessageVO> chatMessageVOS = returnMessage(loginUser, team.getUserId(), chatLambdaQueryWrapper);
        saveCache(CACHE_CHAT_TEAM, String.valueOf(teamId), chatMessageVOS);
        return chatMessageVOS;
    }

    /**
     * 获得大厅聊天
     *
     * @param chatType  聊天类型
     * @param loginUser 登录用户
     * @return {@link List}<{@link ChatMessageVO}>
     */
    @Override
    public List<ChatMessageVO> getHallChat(int chatType, User loginUser) {
        List<ChatMessageVO> chatRecords = getCache(CACHE_CHAT_HALL, String.valueOf(loginUser.getId()));
        if (chatRecords != null) {
            List<ChatMessageVO> chatMessageVOS = checkIsMyMessage(loginUser, chatRecords);
            saveCache(CACHE_CHAT_HALL, String.valueOf(loginUser.getId()), chatMessageVOS);
            return chatMessageVOS;
        }
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.eq(Chat::getChatType, chatType);
        List<ChatMessageVO> chatMessageVOS = returnMessage(loginUser, null, chatLambdaQueryWrapper);
        saveCache(CACHE_CHAT_HALL, String.valueOf(loginUser.getId()), chatMessageVOS);
        return chatMessageVOS;
    }


    /**
     * 获取私聊列表
     *
     * @param userId id
     * @return {@link List}<{@link UserVO}>
     */
    @Override
    public List<PrivateChatVO> getPrivateList(Long userId) {
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.eq(Chat::getFromId, userId).eq(Chat::getChatType, PRIVATE_CHAT);
        List<Chat> mySend = this.list(chatLambdaQueryWrapper);
        HashSet<Long> userIdSet = new HashSet<>();
        mySend.forEach((chat) -> {
            Long toId = chat.getToId();
            userIdSet.add(toId);
        });
        chatLambdaQueryWrapper.clear();
        chatLambdaQueryWrapper.eq(Chat::getToId, userId).eq(Chat::getChatType, PRIVATE_CHAT);
        List<Chat> myReceive = this.list(chatLambdaQueryWrapper);
        myReceive.forEach((chat) -> {
            Long fromId = chat.getFromId();
            userIdSet.add(fromId);
        });
        List<User> userList = userService.listByIds(userIdSet);
        return userList.stream().map((user) -> {
            PrivateChatVO privateChatVO = new PrivateChatVO();
            privateChatVO.setUserId(user.getId());
            privateChatVO.setUsername(user.getUsername());
            privateChatVO.setAvatarUrl(user.getAvatarUrl());
            Pair<String, Date> pair = getPrivateLastMessage(userId, user.getId());
            privateChatVO.setLastMessage(pair.getKey());
            privateChatVO.setLastMessageDate(pair.getValue());
            privateChatVO.setUnReadNum(getUnreadNum(userId, user.getId()));
            return privateChatVO;
        }).sorted().collect(Collectors.toList());
    }

    /**
     * 获取私聊未读消息数量
     *
     * @param userId id
     * @return {@link Integer}
     */
    @Override
    public Integer getUnReadPrivateNum(Long userId) {
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.eq(Chat::getToId, userId).eq(Chat::getChatType, PRIVATE_CHAT)
                .eq(Chat::getIsRead, 0);
        return Math.toIntExact(this.count(chatLambdaQueryWrapper));
    }

    /**
     * 阅读私聊消息
     *
     * @param loginId  登录id
     * @param remoteId 遥远id
     * @return {@link Boolean}
     */
    @Override
    public Boolean readPrivateMessage(Long loginId, Long remoteId) {
        LambdaUpdateWrapper<Chat> chatLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        chatLambdaUpdateWrapper.eq(Chat::getFromId, remoteId)
                .eq(Chat::getToId, loginId)
                .eq(Chat::getChatType, PRIVATE_CHAT)
                .set(Chat::getIsRead, 1);
        return this.update(chatLambdaUpdateWrapper);
    }

    /**
     * 获取未读消息数量
     *
     * @param loginId  登录id
     * @param remoteId 遥远id
     * @return {@link Integer}
     */
    private Integer getUnreadNum(Long loginId, Long remoteId) {
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper.eq(Chat::getFromId, remoteId)
                .eq(Chat::getToId, loginId)
                .eq(Chat::getChatType, PRIVATE_CHAT)
                .eq(Chat::getIsRead, 0);
        return Math.toIntExact(this.count(chatLambdaQueryWrapper));
    }

    /**
     * 获取私聊最后一条消息信息
     *
     * @param loginId  登录id
     * @param remoteId 遥远id
     * @return {@link String}
     */
    private Pair<String, Date> getPrivateLastMessage(Long loginId, Long remoteId) {
        LambdaQueryWrapper<Chat> chatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chatLambdaQueryWrapper
                .eq(Chat::getFromId, loginId)
                .eq(Chat::getToId, remoteId)
                .eq(Chat::getChatType, PRIVATE_CHAT)
                .orderBy(true, false, Chat::getCreateTime);
        List<Chat> chatList1 = this.list(chatLambdaQueryWrapper);
        chatLambdaQueryWrapper.clear();
        chatLambdaQueryWrapper.eq(Chat::getFromId, remoteId)
                .eq(Chat::getToId, loginId)
                .eq(Chat::getChatType, PRIVATE_CHAT)
                .orderBy(true, false, Chat::getCreateTime);
        List<Chat> chatList2 = this.list(chatLambdaQueryWrapper);
        if (chatList1.isEmpty() && chatList2.isEmpty()) {
            return new Pair<>("", null);
        }
        if (chatList1.isEmpty()) {
            return new Pair<>(chatList2.get(0).getText(), chatList2.get(0).getCreateTime());
        }
        if (chatList2.isEmpty()) {
            return new Pair<>(chatList1.get(0).getText(), chatList1.get(0).getCreateTime());
        }
        if (chatList1.get(0).getCreateTime().after(chatList2.get(0).getCreateTime())) {
            return new Pair<>(chatList1.get(0).getText(), chatList1.get(0).getCreateTime());
        } else {
            return new Pair<>(chatList2.get(0).getText(), chatList2.get(0).getCreateTime());
        }
    }

    /**
     * 支票是我信息
     *
     * @param loginUser   登录用户
     * @param chatRecords 聊天记录
     * @return {@link List}<{@link ChatMessageVO}>
     */
    private List<ChatMessageVO> checkIsMyMessage(User loginUser, List<ChatMessageVO> chatRecords) {
        return chatRecords.stream().peek(chat -> {
            if (chat.getFromUser().getId() != loginUser.getId() && chat.getIsMy()) {
                chat.setIsMy(false);
            }
            if (chat.getFromUser().getId() == loginUser.getId() && !chat.getIsMy()) {
                chat.setIsMy(true);
            }
        }).collect(Collectors.toList());
    }

    /**
     * 返回消息
     *
     * @param loginUser              登录用户
     * @param userId                 用户id
     * @param chatLambdaQueryWrapper 聊天lambda查询包装器
     * @return {@link List}<{@link ChatMessageVO}>
     */
    private List<ChatMessageVO> returnMessage(User loginUser,
                                              Long userId,
                                              LambdaQueryWrapper<Chat> chatLambdaQueryWrapper) {
        List<Chat> chatList = this.list(chatLambdaQueryWrapper);
        return chatList.stream().map(chat -> {
            ChatMessageVO chatMessageVo = chatResult(chat.getFromId(), chat.getText(), chat.getMessageType());
            boolean isCaptain = userId != null && userId.equals(chat.getFromId());
            if (userService.getById(chat.getFromId()).getRole() == ADMIN_ROLE || isCaptain) {
                chatMessageVo.setIsAdmin(true);
            }
            if (chat.getFromId().equals(loginUser.getId())) {
                chatMessageVo.setIsMy(true);
            }
            chatMessageVo.setCreateTime(DateUtil.format(chat.getCreateTime(), "yyyy年MM月dd日 HH:mm:ss"));
            return chatMessageVo;
        }).collect(Collectors.toList());
    }
}




