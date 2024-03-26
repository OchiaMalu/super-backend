package net.zjitc.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import net.zjitc.common.ErrorCode;
import net.zjitc.exception.BusinessException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static net.zjitc.constants.SystemConstants.DEFAULT_BUFFER_SIZE;
import static net.zjitc.constants.SystemConstants.FILE_END;

/**
 * 文件工具
 *
 * @author OchiaMalu
 * @date 2023/06/22
 */
@Slf4j
@Component
public class FileUtils {

    private static String basePath;

    /**
     * 上传文件到本地
     *
     * @param file 文件
     * @return {@link String}
     */
    public static String uploadFile2Local(MultipartFile file) {
        File localFile = transferFile(file);
        return localFile.getName();
    }

    /**
     * 上传文件到云端
     *
     * @param file 文件
     * @return {@link String}
     */
    public static String uploadFile2Cloud(MultipartFile file) {
        File localFile = transferFile(file);
        byte[] imageStream = getImageStream(localFile);
        String fileName = QiNiuUtils.upload(imageStream);
        boolean delete = localFile.delete();
        if (!delete) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        //上传七牛云
        return fileName;
    }

    /**
     * 获取图像流
     *
     * @param imageFile 图像文件
     * @return {@link byte[]}
     */
    public static byte[] getImageStream(File imageFile) {
        byte[] buffer = null;
        FileInputStream fis;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            fis = new FileInputStream(imageFile);
            byte[] b = new byte[DEFAULT_BUFFER_SIZE];
            int n;
            while ((n = fis.read(b)) != FILE_END) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            log.error("exception message", e);
        }
        return buffer;
    }

    /**
     * 传输文件
     *
     * @param file 文件
     * @return {@link File}
     */
    public static File transferFile(MultipartFile file) {
        String uid = UUID.randomUUID().toString(true);
        //获取原文件名
        String originalFilename = file.getOriginalFilename();
        if (Strings.isEmpty(originalFilename)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //获取后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (Strings.isEmpty(suffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        File dir = new File(System.getProperty("user.dir") + basePath);
        //如果文件夹不存在则新建文件夹
        if (!dir.exists()) {
            boolean mkdir = dir.mkdir();
            if (!mkdir) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
        File localFile = new File(System.getProperty("user.dir") + basePath + uid + suffix);
        try {
            //将文件从tomcat临时目录转移到指定的目录
            file.transferTo(localFile);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        String newFileName = DigestUtil.md5Hex(localFile) + suffix;
        File newFile = new File(System.getProperty("user.dir") + basePath + newFileName);
        if (newFile.exists()) {
            boolean deleted = localFile.delete();
            if (!deleted) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            boolean renamed = localFile.renameTo(newFile);
            if (!renamed) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
        return newFile;
    }

    /**
     * init基本路径
     *
     * @param b b
     */
    @Value("${super.img}")
    public void initBasePath(String b) {
        basePath = b;
    }
}
