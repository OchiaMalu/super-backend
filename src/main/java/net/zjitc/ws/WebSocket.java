package net.zjitc.ws;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.zjitc.config.HttpSessionConfigurator;
import net.zjitc.model.domain.Chat;
import net.zjitc.model.domain.Team;
import net.zjitc.model.request.MessageRequest;
import net.zjitc.model.vo.MessageVO;
import net.zjitc.model.vo.WebSocketVO;
import net.zjitc.service.ChatService;
import net.zjitc.service.TeamService;
import net.zjitc.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import net.zjitc.model.domain.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static net.zjitc.constants.ChatConstant.*;
import static net.zjitc.constants.UserConstants.ADMIN_ROLE;
import static net.zjitc.constants.UserConstants.USER_LOGIN_STATE;
import static net.zjitc.utils.StringUtils.stringJsonListToLongSet;


@Component
@Slf4j
@ServerEndpoint(value = "/websocket/{userId}/{teamId}", configurator = HttpSessionConfigurator.class)
public class WebSocket {
    /**
     * 保存队伍的连接信息
     */
    private static final Map<String, ConcurrentHashMap<String, WebSocket>> ROOMS = new HashMap<>();
    /**
     * 线程安全的无序的集合
     */
    private static final CopyOnWriteArraySet<Session> SESSIONS = new CopyOnWriteArraySet<>();
    /**
     * 存储在线连接数
     */
    private static final Map<String, Session> SESSION_POOL = new HashMap<>(0);
    private static UserService userService;
    private static ChatService chatService;
    private static TeamService teamService;
    /**
     * 房间在线人数
     */
    private static int onlineCount = 0;
    /**
     * 当前信息
     */
    private Session session;
    private HttpSession httpSession;

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    @Resource
    public void setHeatMapService(UserService userService) {
        WebSocket.userService = userService;
    }

    @Resource
    public void setHeatMapService(ChatService chatService) {
        WebSocket.chatService = chatService;
    }

    @Resource
    public void setHeatMapService(TeamService teamService) {
        WebSocket.teamService = teamService;
    }


    /**
     * 队伍内群发消息
     *
     * @param teamId
     * @param msg
     * @throws Exception
     */
    public static void broadcast(String teamId, String msg) {
        ConcurrentHashMap<String, WebSocket> map = ROOMS.get(teamId);
        // keySet获取map集合key的集合  然后在遍历key即可
        for (String key : map.keySet()) {
            try {
                WebSocket webSocket = map.get(key);
                webSocket.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送消息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId, @PathParam(value = "teamId") String teamId, EndpointConfig config) {
        try {
            if (StringUtils.isBlank(userId) || "undefined".equals(userId)) {
                sendError(userId, "参数有误");
                return;
            }
            HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
            User user = (User) httpSession.getAttribute(USER_LOGIN_STATE);
            if (user != null) {
                this.session = session;
                this.httpSession = httpSession;
            }
            if (!"NaN".equals(teamId)) {
                if (!ROOMS.containsKey(teamId)) {
                    ConcurrentHashMap<String, WebSocket> room = new ConcurrentHashMap<>(0);
                    room.put(userId, this);
                    ROOMS.put(String.valueOf(teamId), room);
                    // 在线数加1
                    addOnlineCount();
                } else {
                    if (!ROOMS.get(teamId).containsKey(userId)) {
                        ROOMS.get(teamId).put(userId, this);
                        // 在线数加1
                        addOnlineCount();
                    }
                }
                log.info("有新连接加入！当前在线人数为" + getOnlineCount());
            } else {
                SESSIONS.add(session);
                SESSION_POOL.put(userId, session);
                log.info("有新用户加入，userId={}, 当前在线人数为：{}", userId, SESSION_POOL.size());
                sendAllUsers();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId, @PathParam(value = "teamId") String teamId, Session session) {
        try {
            if (!"NaN".equals(teamId)) {
                ROOMS.get(teamId).remove(userId);
                if (getOnlineCount() > 0) {
                    subOnlineCount();
                }
                log.info("用户退出:当前在线人数为:" + getOnlineCount());
            } else {
                if (!SESSION_POOL.isEmpty()) {
                    SESSION_POOL.remove(userId);
                    SESSIONS.remove(session);
                }
                log.info("【WebSocket消息】连接断开，总数为：" + SESSION_POOL.size());
                sendAllUsers();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, @PathParam("userId") String userId) {
        if ("PING".equals(message)) {
            sendOneMessage(userId, "pong");
            log.error("心跳包，发送给={},在线:{}人", userId, getOnlineCount());
            return;
        }
        log.info("服务端收到用户username={}的消息:{}", userId, message);
        MessageRequest messageRequest = new Gson().fromJson(message, MessageRequest.class);
        Long toId = messageRequest.getToId();
        Long teamId = messageRequest.getTeamId();
        String text = messageRequest.getText();
        Integer chatType = messageRequest.getChatType();
        User fromUser = userService.getById(userId);
        Team team = teamService.getById(teamId);
        if (chatType == PRIVATE_CHAT) {
            // 私聊
            privateChat(fromUser, toId, text, chatType);
        } else if (chatType == TEAM_CHAT) {
            // 队伍内聊天
            teamChat(fromUser, text, team, chatType);
        } else {
            // 群聊
            hallChat(fromUser, text, chatType);
        }
    }

    /**
     * 队伍聊天
     *
     * @param user
     * @param text
     * @param team
     * @param chatType
     */
    private void teamChat(User user, String text, Team team, Integer chatType) {
        MessageVO MessageVO = new MessageVO();
        WebSocketVO fromWebSocketVO = new WebSocketVO();
        BeanUtils.copyProperties(user, fromWebSocketVO);
        MessageVO.setFormUser(fromWebSocketVO);
        MessageVO.setText(text);
        MessageVO.setTeamId(team.getId());
        MessageVO.setChatType(chatType);
        MessageVO.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        if (user.getId() == team.getUserId() || user.getRole() == ADMIN_ROLE) {
            MessageVO.setIsAdmin(true);
        }
        User loginUser = (User) this.httpSession.getAttribute(USER_LOGIN_STATE);
        if (loginUser.getId() == user.getId()) {
            MessageVO.setIsMy(true);
        }
        String toJson = new Gson().toJson(MessageVO);
        try {
            broadcast(String.valueOf(team.getId()), toJson);
            savaChat(user.getId(), null, text, team.getId(), chatType);
            chatService.deleteKey(CACHE_CHAT_TEAM, String.valueOf(team.getId()));
            log.error("队伍聊天，发送给={},队伍={},在线:{}人", user.getId(), team.getId(), getOnlineCount());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 大厅聊天
     *
     * @param user
     * @param text
     */
    private void hallChat(User user, String text, Integer chatType) {
        MessageVO MessageVO = new MessageVO();
        WebSocketVO fromWebSocketVO = new WebSocketVO();
        BeanUtils.copyProperties(user, fromWebSocketVO);
        MessageVO.setFormUser(fromWebSocketVO);
        MessageVO.setText(text);
        MessageVO.setChatType(chatType);
        MessageVO.setCreateTime(DateUtil.format(new Date(), "yyyy年MM月dd日 HH:mm:ss"));
        if (user.getRole() == ADMIN_ROLE) {
            MessageVO.setIsAdmin(true);
        }
        User loginUser = (User) this.httpSession.getAttribute(USER_LOGIN_STATE);
        if (loginUser.getId() == user.getId()) {
            MessageVO.setIsMy(true);
        }
        String toJson = new Gson().toJson(MessageVO);
        sendAllMessage(toJson);
        savaChat(user.getId(), null, text, null, chatType);
        chatService.deleteKey(CACHE_CHAT_HALL, String.valueOf(user.getId()));
    }

    /**
     * 私聊
     *
     * @param user
     * @param text
     */
    private void privateChat(User user, Long toId, String text, Integer chatType) {
        Session toSession = SESSION_POOL.get(toId.toString());
        if (toSession != null) {
            MessageVO MessageVO = chatService.chatResult(user.getId(), toId, text, chatType, DateUtil.date(System.currentTimeMillis()));
            User loginUser = (User) this.httpSession.getAttribute(USER_LOGIN_STATE);
            if (loginUser.getId() == user.getId()) {
                MessageVO.setIsMy(true);
            }
            String toJson = new Gson().toJson(MessageVO);
            sendOneMessage(toId.toString(), toJson);
            savaChat(user.getId(), toId, text, null, chatType);
            log.info("发送给用户username={}，消息：{}", MessageVO.getToUser(), toJson);
        } else {
            log.info("发送失败，未找到用户username={}的session", toId);
        }
        chatService.deleteKey(CACHE_CHAT_PRIVATE, user.getId() + "" + toId);
        chatService.deleteKey(CACHE_CHAT_PRIVATE, toId + "" + user.getId());
    }

    /**
     * 保存聊天
     *
     * @param userId
     * @param toId
     * @param text
     */
    private void savaChat(Long userId, Long toId, String text, Long teamId, Integer chatType) {
        if (chatType == PRIVATE_CHAT) {
            User user = userService.getById(userId);
            Set<Long> userIds = stringJsonListToLongSet(user.getFriendIds());
            if (!userIds.contains(toId)) {
                sendError(String.valueOf(userId), "该用户不是你的好友");
                return;
            }
        }
        Chat chat = new Chat();
        chat.setFromId(userId);
        chat.setText(String.valueOf(text));
        chat.setChatType(chatType);
        chat.setCreateTime(new Date());
        if (toId != null && toId > 0) {
            chat.setToid(toId);
        }
        if (teamId != null && teamId > 0) {
            chat.setTeamId(teamId);
        }
        chatService.save(chat);
    }

    /**
     * 发送失败
     *
     * @param userId
     * @param errorMessage
     */
    private void sendError(String userId, String errorMessage) {
        JSONObject obj = new JSONObject();
        obj.set("error", errorMessage);
        sendOneMessage(userId, obj.toString());
    }

    /**
     * 此为广播消息
     *
     * @param message 消息
     */
    public void sendAllMessage(String message) {
        log.info("【WebSocket消息】广播消息：" + message);
        for (Session session : SESSIONS) {
            try {
                if (session.isOpen()) {
                    synchronized (session) {
                        session.getBasicRemote().sendText(message);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 此为单点消息
     *
     * @param userId  用户编号
     * @param message 消息
     */
    public void sendOneMessage(String userId, String message) {
        Session session = SESSION_POOL.get(userId);
        if (session != null && session.isOpen()) {
            try {
                synchronized (session) {
                    log.info("【WebSocket消息】单点消息：" + message);
                    session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送所有在线用户信息
     */
    public void sendAllUsers() {
        log.info("【WebSocket消息】发送所有在线用户信息");
        HashMap<String, List<WebSocketVO>> stringListHashMap = new HashMap<>(0);
        List<WebSocketVO> WebSocketVOs = new ArrayList<>();
        stringListHashMap.put("users", WebSocketVOs);
        for (Serializable key : SESSION_POOL.keySet()) {
            User user = userService.getById(key);
            WebSocketVO WebSocketVO = new WebSocketVO();
            BeanUtils.copyProperties(user, WebSocketVO);
            WebSocketVOs.add(WebSocketVO);
        }
        sendAllMessage(JSONUtil.toJsonStr(stringListHashMap));
    }
}
