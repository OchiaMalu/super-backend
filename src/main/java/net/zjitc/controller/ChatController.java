package net.zjitc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.zjitc.common.BaseResponse;
import net.zjitc.common.ErrorCode;
import net.zjitc.common.ResultUtils;
import net.zjitc.exception.BusinessException;
import net.zjitc.model.request.ChatRequest;
import net.zjitc.service.ChatService;
import net.zjitc.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import net.zjitc.model.domain.User;
import net.zjitc.model.vo.MessageVO;

import static net.zjitc.constants.ChatConstant.*;
import static net.zjitc.constants.UserConstants.USER_LOGIN_STATE;

/**
 * 聊天控制器
 *
 * @author 林哲好
 * @date 2023/06/19
 */
@RestController
@RequestMapping("/chat")
@Api(tags = "聊天管理模块")
public class ChatController {
    /**
     * 聊天服务
     */
    @Resource
    private ChatService chatService;

    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    /**
     * 让私人聊天
     *
     * @param chatRequest 聊天请求
     * @param request     请求
     * @return {@link BaseResponse}<{@link List}<{@link MessageVO}>>
     */
    @PostMapping("/privateChat")
    @ApiOperation(value = "获取私聊")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "chatRequest", value = "聊天请求"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<MessageVO>> getPrivateChat(@RequestBody ChatRequest chatRequest, HttpServletRequest request) {
        if (chatRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        List<MessageVO> privateChat = chatService.getPrivateChat(chatRequest, PRIVATE_CHAT, loginUser);
        return ResultUtils.success(privateChat);
    }

    /**
     * 得到团队聊天
     *
     * @param chatRequest 聊天请求
     * @param request     请求
     * @return {@link BaseResponse}<{@link List}<{@link MessageVO}>>
     */
    @PostMapping("/teamChat")
    @ApiOperation(value = "获取队伍聊天")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "chatRequest", value = "聊天请求"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<MessageVO>> getTeamChat(@RequestBody ChatRequest chatRequest, HttpServletRequest request) {
        if (chatRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求有误");
        }
        User loginUser = userService.getLoginUser(request);
        List<MessageVO> teamChat = chatService.getTeamChat(chatRequest, TEAM_CHAT, loginUser);
        return ResultUtils.success(teamChat);
    }

    @GetMapping("/hallChat")
    public BaseResponse<List<MessageVO>> getHallChat(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<MessageVO> hallChat = chatService.getHallChat(HALL_CHAT, loginUser);
        return ResultUtils.success(hallChat);
    }
}