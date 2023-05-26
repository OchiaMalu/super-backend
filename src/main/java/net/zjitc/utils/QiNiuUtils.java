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

public class QiNiuUtils {
    public static void upload(byte[] uploadBytes, String fileName) {
        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = "YEbtH-gNUUMXLQ1ZJkU9ZYXxI_KD3eLQdEPoN5Uq";
        String secretKey = "AF_TexI1P0viU15b1H57gTxX-n9CeuGTsOGISbl4";
        String bucket = "picgo-ochiamalu";

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(byteInputStream, fileName, upToken, null, null);
        } catch (QiniuException ex) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "头像上传失败");
        }
    }
}
