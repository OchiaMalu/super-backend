package top.ochiamalu.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ochiamalu.common.BaseResponse;
import top.ochiamalu.common.ErrorCode;
import top.ochiamalu.common.ResultUtils;
import top.ochiamalu.exception.BusinessException;
import top.ochiamalu.model.domain.User;
import top.ochiamalu.service.EmoticonService;
import top.ochiamalu.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 表情控制器
 *
 * @author ochiamalu
 * @date 2025/03/03
 */
@RestController
@RequestMapping("/emoticon")
public class EmoticonController {

    @Resource
    private EmoticonService emoticonService;

    @Resource
    private UserService userService;

    /**
     * 获取表情
     *
     * @param request 请求
     * @return {@link BaseResponse }<{@link List }<{@link String }>>
     */
    @GetMapping
    @ApiOperation(value = "获取表情")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<String>> getEmoticonList(HttpServletRequest request) {
        List<String> emoticonList = emoticonService.getEmoticonList(request);
        return ResultUtils.success(emoticonList);
    }

    /**
     * 添加表情
     *
     * @param url     URL
     * @param request 请求
     * @return {@link BaseResponse }<{@link Boolean }>
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加表情")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "url", value = "表情url"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> addEmoticon(String url, HttpServletRequest request) {
        if (url == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请上传文件");
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "请登录");
        }
        boolean success = emoticonService.addEmoticon(url, loginUser.getId());
        return ResultUtils.success(success);
    }

    /**
     * 删除表情
     *
     * @param url     URL
     * @param request 请求
     * @return {@link BaseResponse }<{@link Boolean }>
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除表情")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "url", value = "表情url"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> deleteEmoticon(String url, HttpServletRequest request) {
        if (url == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请上传文件");
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "请登录");
        }
        boolean result = emoticonService.delete(url, loginUser.getId());
        return ResultUtils.success(result);
    }
}
