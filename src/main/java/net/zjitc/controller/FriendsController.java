package net.zjitc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.zjitc.common.BaseResponse;
import net.zjitc.common.ErrorCode;
import net.zjitc.common.ResultUtils;
import net.zjitc.exception.BusinessException;
import net.zjitc.model.request.FriendAddRequest;
import net.zjitc.service.FriendsService;
import net.zjitc.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.zjitc.model.domain.User;

/**
 * 朋友控制器
 *
 * @author 林哲好
 * @date 2023/06/19
 */
@RestController
@RequestMapping("/friends")
@Api(tags = "好友管理模块")
public class FriendsController {
    /**
     * 朋友服务
     */
    @Resource
    private FriendsService friendsService;

    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    /**
     * 添加朋友记录
     *
     * @param friendAddRequest 好友添加请求
     * @param request          请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/add")
    @ApiOperation(value = "获取博文")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "friendAddRequest", value = "好友添加请求"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> addFriendRecords(@RequestBody FriendAddRequest friendAddRequest, HttpServletRequest request) {
        if (friendAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求有误");
        }
        User loginUser = userService.getLoginUser(request);
        boolean addStatus = friendsService.addFriendRecords(loginUser, friendAddRequest);
        return ResultUtils.success(addStatus);
    }
}
