package net.zjitc.utils;

import net.zjitc.properties.SuperProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author OchiaMalu
 */
@Component
public class MessageUtils {
    private static SuperProperties superProperties;

    @Resource
    private SuperProperties tempProperties;


    public static void sendMessage(String phoneNum, String code) {
        if (superProperties.isUseShortMessagingService()) {
            SMSUtils.sendMessage(phoneNum, code);
        } else {
            System.out.println("验证码: " + code);
        }
    }

    @PostConstruct
    public void initProperties() {
        superProperties = tempProperties;
    }
}
