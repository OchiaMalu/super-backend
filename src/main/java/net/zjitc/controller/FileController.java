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
import net.zjitc.service.UserService;
import net.zjitc.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

import static net.zjitc.constants.SystemConstants.QiNiuUrl;

/**
 * 文件控制器
 *
 * @author 林哲好
 * @date 2023/06/11
 */
@RestController
@RequestMapping("/common")
@CrossOrigin("http://localhost:5173")
@Api(tags = "文件管理模块")
public class FileController {

    /**
     * 基本路径
     *///图片保存路径
    @Value("${super.img}")
    private String basePath;

    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    /**
     * 上传
     *
     * @param file    文件
     * @param request 请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "file", value = "文件"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> upload(MultipartFile file, HttpServletRequest request) {
        if (file == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请上传文件");
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "请登录");
        }
        String filename = FileUtils.uploadFile(file);
        String fileUrl = QiNiuUrl + filename;
        User user = new User();
        user.setId(loginUser.getId());
        user.setAvatarUrl(fileUrl);
        boolean success = userService.updateById(user);
        if (!success) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "头像上传失败");
        }
        return ResultUtils.success(fileUrl);
    }

    /**
     * 下载
     *
     * @param name     名字
     * @param response 响应
     */
    @GetMapping("/download")
    @ApiOperation(value = "文件下载")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "name", value = "文件名"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public void download(String name, HttpServletResponse response) {
        try {
            //获取指定文件
            File img = new File(System.getProperty("user.dir") + basePath + name);
            //获取输入流
            FileInputStream inputStream = new FileInputStream(img);
            //获取输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //指定response类型
            response.setContentType("image/jpeg");
            int len = 0;
            //设置缓冲区大小
            byte[] bytes = new byte[1024];
            //将文件从输入流读到缓冲区，输出流读取缓冲区内容
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}