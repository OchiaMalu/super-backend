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
import net.zjitc.model.request.FriendAddRequest;
import net.zjitc.model.vo.FriendsRecordVO;
import net.zjitc.service.FriendsService;
import net.zjitc.service.UserService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * 好友控制器
 *
 * @author OchiaMalu
 * @date 2023/06/19
 */
@RestController
@RequestMapping("/friends")
@Api(tags = "好友管理模块")
public class FriendsController {
    /**
     * 好友服务
     */
    @Resource
    private FriendsService friendsService;

    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    /**
     * 添加好友
     *
     * @param friendAddRequest 好友添加请求
     * @param request          请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加好友")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "friendAddRequest", value = "好友添加请求"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> addFriendRecords(
            @RequestBody FriendAddRequest friendAddRequest,
            HttpServletRequest request) {
        if (friendAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求有误");
        }
        User loginUser = userService.getLoginUser(request);
        boolean addStatus = friendsService.addFriendRecords(loginUser, friendAddRequest);
        return ResultUtils.success(addStatus);
    }

    /**
     * 查询记录
     *
     * @param request 请求
     * @return {@link BaseResponse}<{@link List}<{@link FriendsRecordVO}>>
     */
    @GetMapping("/getRecords")
    @ApiOperation(value = "查询记录")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<FriendsRecordVO>> getRecords(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<FriendsRecordVO> friendsList = friendsService.obtainFriendApplicationRecords(loginUser);
        return ResultUtils.success(friendsList);
    }

    /**
     * 获取未读记录条数
     *
     * @param request 请求
     * @return {@link BaseResponse}<{@link Integer}>
     */
    @GetMapping("/getRecordCount")
    @ApiOperation(value = "查询记录")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Integer> getRecordCount(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        int recordCount = friendsService.getRecordCount(loginUser);
        return ResultUtils.success(recordCount);
    }

    /**
     * 获取我申请的记录
     *
     * @param request 请求
     * @return {@link BaseResponse}<{@link List}<{@link FriendsRecordVO}>>
     */
    @GetMapping("/getMyRecords")
    @ApiOperation(value = "获取我申请的记录")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<FriendsRecordVO>> getMyRecords(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<FriendsRecordVO> myFriendsList = friendsService.getMyRecords(loginUser);
        return ResultUtils.success(myFriendsList);
    }

    /**
     * 同意申请
     *
     * @param fromId  从id
     * @param request 请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/agree/{fromId}")
    @ApiOperation(value = "同意申请")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "fromId", value = "申请id"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> agreeToApply(@PathVariable("fromId") Long fromId, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        boolean agreeToApplyStatus = friendsService.agreeToApply(loginUser, fromId);
        return ResultUtils.success(agreeToApplyStatus);
    }

    /**
     * 取消申请
     *
     * @param id      id
     * @param request 请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/canceledApply/{id}")
    @ApiOperation(value = "取消申请")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "申请id"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> canceledApply(@PathVariable("id") Long id, HttpServletRequest request) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求有误");
        }
        User loginUser = userService.getLoginUser(request);
        boolean canceledApplyStatus = friendsService.canceledApply(id, loginUser);
        return ResultUtils.success(canceledApplyStatus);
    }

    /**
     * 阅读
     *
     * @param ids     id
     * @param request 请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @GetMapping("/read")
    @ApiOperation(value = "阅读")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "ids", value = "申请id"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> toRead(@RequestParam(required = false) Set<Long> ids, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(ids)) {
            return ResultUtils.success(false);
        }
        User loginUser = userService.getLoginUser(request);
        boolean isRead = friendsService.toRead(loginUser, ids);
        return ResultUtils.success(isRead);
    }
}
