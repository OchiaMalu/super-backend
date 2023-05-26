package net.zjitc.controller;

import cn.hutool.core.lang.UUID;
import net.zjitc.common.BaseResponse;
import net.zjitc.common.ErrorCode;
import net.zjitc.common.ResultUtils;
import net.zjitc.exception.BusinessException;
import net.zjitc.model.domain.User;
import net.zjitc.service.UserService;
import net.zjitc.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static net.zjitc.constants.SystemConstants.QiNiuUrl;

@RestController
@RequestMapping("/common")
@CrossOrigin("http://localhost:5173")
public class FileController {

    //图片保存路径
    @Value("${super.img}")
    private String basePath;

    @Resource
    private UserService userService;

    @PostMapping("/upload")
    public BaseResponse<String> upload(MultipartFile file, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        //获取原文件名
        String originalFilename = file.getOriginalFilename();
        //获取后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //设置新文件名
        String filename = UUID.randomUUID().toString() + suffix;
        File dir = new File(System.getProperty("user.dir") + basePath);
        //如果文件夹不存在则新建文件夹
        if (!dir.exists()) {
            dir.mkdir();
        }
        File localFile = new File(System.getProperty("user.dir") + basePath + filename);
        try {
            //将文件从tomcat临时目录转移到指定的目录
            file.transferTo(localFile);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        byte[] imageStream = getImageStream(localFile);
        QiNiuUtils.upload(imageStream, filename);
        localFile.delete();
        String fileUrl = QiNiuUrl + filename;
        User user = new User();
        user.setId(loginUser.getId());
        user.setAvatarUrl(fileUrl);
        boolean success = userService.updateById(user);
        if (!success){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"头像上传失败");
        }
        return ResultUtils.success(fileUrl);
    }

    @GetMapping("/download")
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

    public static byte[] getImageStream(File imageFile) {
        byte[] buffer = null;
        FileInputStream fis;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            fis = new FileInputStream(imageFile);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}