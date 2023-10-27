package net.zjitc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author OchiaMalu
 */
@Data
@Component
@ConfigurationProperties(prefix = "super.sms")
public class SMSProperties {

    /**
     * 地域ID
     */
    private String regionId;

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 秘密密钥
     */
    private String secretKey;

    /**
     * 短信签名名称
     */
    private String signName;

    /**
     * 短信模板CODE
     */
    private String templateCode;

    /**
     * 短信模板变量对应的实际值
     */
    private String templateParam;

}
