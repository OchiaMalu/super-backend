package net.zjitc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.zjitc.model.domain.Chat;
import net.zjitc.model.domain.User;
import net.zjitc.model.request.ChatRequest;
import net.zjitc.model.vo.MessageVO;

import java.util.Date;
import java.util.List;

/**
* @author OchiaMalu
* @description 针对表【chat(聊天消息表)】的数据库操作Service
* @createDate 2023-06-17 21:50:15
*/
public interface ChatService extends IService<Chat> {
     List<MessageVO> getPrivateChat(ChatRequest chatRequest, int chatType, User loginUser);

     List<MessageVO> getCache(String redisKey, String id);

     void saveCache(String redisKey, String id, List<MessageVO> messageVos);

     MessageVO chatResult(Long userId, Long toId, String text, Integer chatType, Date createTime);

     void deleteKey(String key, String id);
}
