package net.zjitc.utils;

import cn.hutool.core.lang.UUID;
import net.zjitc.common.BaseResponse;
import net.zjitc.common.ResultUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/common")
@CrossOrigin("http://localhost:5173")
public class FileUtil {

    //图片保存路径
    @Value("${super.img}")
    private String basePath;

    @PostMapping("/upload")
    public BaseResponse<String> upload(MultipartFile file) {
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
        try {
            //将文件从tomcat临时目录转移到指定的目录
            file.transferTo(new File(System.getProperty("user.dir") + basePath + filename));
            return ResultUtils.success(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}