package net.zjitc.utils;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import net.zjitc.common.ErrorCode;
import net.zjitc.exception.BusinessException;

import java.io.ByteArrayInputStream;

/**
 * 七牛云工具
 *
 * @author 林哲好
 * @date 2023/06/22
 */
public class QiNiuUtils {
    public static String upload(byte[] uploadBytes, String fileName) {
        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = "ak";
        String secretKey = "sk";
        String bucket = "bucket";

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(byteInputStream, null, upToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "头像上传失败");
        }
    }
}
