package net.zjitc.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.log4j.Log4j2;
import net.zjitc.properties.SMSProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * smsutil
 * 短信发送工具
 *
 * @author OchiaMalu
 * @date 2023/06/22
 */

@Log4j2
@Component
public class SMSUtils {

    private static SMSProperties smsProperties;

    @Resource
    private SMSProperties tempProperties;

    /**
     * 发送消息
     *
     * @param phoneNum 电话号码
     * @param code     密码
     */
    public static void sendMessage(String phoneNum, String code) {
        IClientProfile profile = DefaultProfile.getProfile(
                smsProperties.getRegionId(),
                smsProperties.getAccessKey(),
                smsProperties.getSecretKey()
        );
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNum);
        request.setSignName(smsProperties.getSignName());
        request.setTemplateCode(smsProperties.getTemplateCode());
        request.setTemplateParam("{" + smsProperties.getTemplateParam() + ":\"" + code + "\"}");
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            log.info("发送结果: " + response.getMessage());
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * init属性
     */
    @PostConstruct
    public void initProperties() {
        smsProperties = tempProperties;
    }
}
