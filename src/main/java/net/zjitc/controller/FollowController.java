package net.zjitc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.zjitc.common.BaseResponse;
import net.zjitc.common.ErrorCode;
import net.zjitc.common.ResultUtils;
import net.zjitc.exception.BusinessException;
import net.zjitc.model.domain.User;
import net.zjitc.service.FollowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static net.zjitc.constants.UserConstants.USER_LOGIN_STATE;

/**
 * 关注控制器
 *
 * @author 林哲好
 * @date 2023/06/11
 */
@RestController
@RequestMapping("/follow")
@Api(tags = "关注管理模块")
public class FollowController {
    /**
     * 遵循服务
     */
    @Resource
    private FollowService followService;

    /**
     * 遵循用户
     *
     * @param id      id
     * @param request 请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @PostMapping("/{id}")
    @ApiOperation(value = "关注用户")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "关注用户id"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> followUser(@PathVariable Long id, HttpServletRequest request){
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        followService.followUser(id,loginUser.getId());
        return ResultUtils.success("ok");
    }

    /**
     * 用户列表跟着我
     *
     * @param request 请求
     * @return {@link BaseResponse}<{@link List}<{@link User}>>
     */
    @GetMapping("/my")
    @ApiOperation(value = "获取关注我的用户")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<User>> listUserFollowedMe(HttpServletRequest request){
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        List<User> userList = followService.listUserFollowedMe(loginUser.getId());
        return ResultUtils.success(userList);
    }
}
