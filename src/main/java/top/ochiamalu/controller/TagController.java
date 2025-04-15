package top.ochiamalu.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ochiamalu.common.BaseResponse;
import top.ochiamalu.common.ErrorCode;
import top.ochiamalu.common.ResultUtils;
import top.ochiamalu.exception.BusinessException;
import top.ochiamalu.model.domain.User;
import top.ochiamalu.model.request.TagAddRequest;
import top.ochiamalu.model.request.TagDeleteRequest;
import top.ochiamalu.model.request.TagUpdateRequest;
import top.ochiamalu.model.vo.TagVO;
import top.ochiamalu.service.TagService;
import top.ochiamalu.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 标签控制器
 *
 * @author ochiamalu
 * @date 2025/04/15
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @Resource
    private UserService userService;

    @GetMapping
    @ApiOperation(value = "获取标签")
    public BaseResponse<List<TagVO>> getTags() {
        List<TagVO> tags = tagService.getTags();
        return ResultUtils.success(tags);
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加标签")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "tagAddRequest", value = "新增标签请求"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> addTag(@RequestBody TagAddRequest tagAddRequest, HttpServletRequest request) {
        checkAdmin(request);
        if (StringUtils.isBlank(tagAddRequest.getText())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = tagService.addTag(tagAddRequest.getText());
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除标签")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "tagDeleteRequest", value = "删除标签请求"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> deleteTag(@RequestBody TagDeleteRequest tagDeleteRequest, HttpServletRequest request) {
        checkAdmin(request);
        boolean result = tagService.removeById(tagDeleteRequest.getId());
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新标签")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "tagUpdateRequest", value = "更新标签请求"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> updateTag(@RequestBody TagUpdateRequest tagUpdateRequest, HttpServletRequest request) {
        checkAdmin(request);
        boolean result = tagService.updateTag(tagUpdateRequest.getId(), tagUpdateRequest.getTags());
        return ResultUtils.success(result);
    }

    private void checkAdmin(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        boolean admin = userService.isAdmin(loginUser);
        if (!admin) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
    }
}
