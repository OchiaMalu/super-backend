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
import net.zjitc.service.ConfigService;
import net.zjitc.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static net.zjitc.constants.UserConstants.ADMIN_ROLE;

/**
 * 配置控制器
 *
 * @author OchiaMalu
 * @date 2024/04/12
 */
@RestController
@RequestMapping("/config")
@Api(tags = "配置管理模块")
public class ConfigController {

    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    /**
     * 配置服务
     */
    @Resource
    private ConfigService configService;

    /**
     * 获取通知
     *
     * @return {@link BaseResponse}<{@link String}>
     */
    @GetMapping("/notice")
    @ApiOperation(value = "获取通知")
    public BaseResponse<String> getNoticeText() {
        String noticeTest = configService.getNoticeTest();
        return ResultUtils.success(noticeTest);
    }

    /**
     * 获取轮播图图片
     *
     * @return {@link BaseResponse}<{@link List}<{@link String}>>
     */
    @GetMapping("/swiper")
    @ApiOperation(value = "获取轮播图")
    public BaseResponse<List<String>> getSwiperImages() {
        List<String> swiperImgs = configService.getSwiperImgs();
        return ResultUtils.success(swiperImgs);
    }

    /**
     * 更新通知文本
     *
     * @param request 请求
     * @param text    文本
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/notice")
    @ApiOperation(value = "更新通知")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "text", value = "文本"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> updateNoticeText(@RequestBody String text, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null || !loginUser.getRole().equals(ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
        }
        configService.updateNoticeText(text);
        return ResultUtils.success(true);
    }

    /**
     * 上传图片
     *
     * @param file    文件
     * @param request 请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传图片")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "file", value = "图片"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> uploadImage(@RequestBody MultipartFile file, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "未登录");
        }
        if (!loginUser.getRole().equals(ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
        }
        configService.uploadImages(file);
        return ResultUtils.success(true);
    }

    /**
     * 删除轮播图图片
     *
     * @param request 请求
     * @param url     url
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/remove")
    @ApiOperation(value = "删除轮播图图片")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "request", value = "request请求"),
                    @ApiImplicitParam(name = "url", value = "图片Url")})
    public BaseResponse<Boolean> removeImage(HttpServletRequest request, @RequestBody String url) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null || !loginUser.getRole().equals(ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
        }
        configService.removeUrl(url);
        return ResultUtils.success(true);
    }
}
