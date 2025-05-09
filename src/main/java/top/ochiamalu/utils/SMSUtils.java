package top.ochiamalu.utils;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dypnsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dypnsapi20170525.models.CheckSmsVerifyCodeRequest;
import com.aliyun.sdk.service.dypnsapi20170525.models.CheckSmsVerifyCodeResponse;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import top.ochiamalu.common.ErrorCode;
import top.ochiamalu.exception.BusinessException;
import top.ochiamalu.properties.SMSProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

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
            client.getAcsResponse(request);
        } catch (ClientException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "请稍后重试");
        }
    }

    public static String sendSmsVerifyCode(String phoneNum, Long codeLength) {
        try {
            StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                    .accessKeyId(smsProperties.getAccessKey())
                    .accessKeySecret(smsProperties.getSecretKey())
                    .build());

            AsyncClient client = AsyncClient.builder()
                    .region("cn-hangzhou")
                    .credentialsProvider(provider)
                    .overrideConfiguration(
                            ClientOverrideConfiguration.create()
                                    .setEndpointOverride("dypnsapi.aliyuncs.com")
                    )
                    .build();

            SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = SendSmsVerifyCodeRequest.builder()
                    .signName("速通互联验证码")
                    .phoneNumber(phoneNum)
                    .templateCode("100001")
                    .codeLength(codeLength)
                    .templateParam("{\"code\":\"##code##\",\"min\":\"5\"}")
                    .returnVerifyCode(true)
                    .build();

            CompletableFuture<SendSmsVerifyCodeResponse> response = client.sendSmsVerifyCode(sendSmsVerifyCodeRequest);
            SendSmsVerifyCodeResponse resp = response.get();
            client.close();
            return resp.getBody().getModel().getVerifyCode();

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "请稍后重试");
        }
    }

    public static Boolean checkSmsVerifyCode(String phoneNum, String code) {
        try {
            StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                    .accessKeyId(smsProperties.getAccessKey())
                    .accessKeySecret(smsProperties.getSecretKey())
                    .build());

            AsyncClient client = AsyncClient.builder()
                    .region("cn-hangzhou")
                    .credentialsProvider(provider)
                    .overrideConfiguration(
                            ClientOverrideConfiguration.create()
                                    .setEndpointOverride("dypnsapi.aliyuncs.com")
                    )
                    .build();

            CheckSmsVerifyCodeRequest checkSmsVerifyCodeRequest = CheckSmsVerifyCodeRequest.builder()
                    .phoneNumber(phoneNum)
                    .verifyCode(code)
                    .build();
            CompletableFuture<CheckSmsVerifyCodeResponse> response = client.checkSmsVerifyCode(checkSmsVerifyCodeRequest);
            String verifyResult = response.get().getBody().getModel().getVerifyResult();
            client.close();
            return "PASS".equals(verifyResult);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "请稍后重试");
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
