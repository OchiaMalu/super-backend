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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * 七牛云工具
 *
 * @author OchiaMalu
 * @date 2023/06/22
 */

@Component
public class QiNiuUtils {

    private static String accessKey;

    private static String secretKey;

    private static String bucket;

    public static String upload(byte[] uploadBytes) {
        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);

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

    @Value("${super.qiniu.access-key}")
    public void initAccessKey(String k){
        accessKey = k;
    }

    @Value("${super.qiniu.secret-key}")
    public void initSecretKey(String k){
        secretKey = k;
    }

    @Value("${super.qiniu.bucket}")
    public void initBucket(String k){
        bucket = k;
    }
}
