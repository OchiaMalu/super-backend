package top.ochiamalu.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ochiamalu.common.BaseResponse;
import top.ochiamalu.common.ErrorCode;
import top.ochiamalu.common.ResultUtils;
import top.ochiamalu.exception.BusinessException;
import top.ochiamalu.model.domain.User;
import top.ochiamalu.model.request.AIRequest;
import top.ochiamalu.service.UserService;
import top.ochiamalu.utils.AIUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * AI 控制器
 *
 * @author OchiaMalu
 * @date 2024/06/01
 */
@RestController
@RequestMapping("/ai")
public class AIController {

    @Resource
    private UserService userService;

    /**
     * 获取 AI 消息
     *
     * @param aiRequest AI请求
     * @return {@link BaseResponse }<{@link String }>
     */
    @PostMapping
    @ApiOperation(value = "获取AI消息")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "addCommentRequest", value = "AI请求"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> getAIMessage(@RequestBody AIRequest aiRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (aiRequest.getMessage().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String aiMessage = AIUtils.getAIMessage(aiRequest.getMessage());
        return ResultUtils.success(aiMessage);
    }
}
