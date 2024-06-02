package top.ochiamalu.utils;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;
import top.ochiamalu.common.ErrorCode;
import top.ochiamalu.exception.BusinessException;
import top.ochiamalu.properties.QiNiuProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.ByteArrayInputStream;

/**
 * 七牛云工具
 *
 * @author OchiaMalu
 * @date 2023/06/22
 */

@Component
public class QiNiuUtils {

    private static QiNiuProperties qiNiuProperties;

    @Resource
    private QiNiuProperties tempProperties;

    /**
     * 上载
     *
     * @param uploadBytes 上载字节
     * @return {@link String}
     */
    public static String upload(byte[] uploadBytes) {
        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
        Auth auth = Auth.create(qiNiuProperties.getAccessKey(), qiNiuProperties.getSecretKey());
        String upToken = auth.uploadToken(qiNiuProperties.getBucket());
        try {
            Response response = uploadManager.put(byteInputStream, null, upToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "头像上传失败");
        }
    }

    /**
     * init属性
     */
    @PostConstruct
    public void initProperties() {
        qiNiuProperties = tempProperties;
    }
}
