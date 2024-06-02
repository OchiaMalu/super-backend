package net.zjitc.config;

import com.zhipu.oapi.ClientV4;
import net.zjitc.properties.SuperProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * AI 配置类
 *
 * @author OchiaMalu
 * @date 2024/06/01
 */
@Configuration
public class AIConfig {

    @Resource
    private SuperProperties superProperties;

    @Bean
    public ClientV4 clientV4() {
        return new ClientV4.Builder(superProperties.getAiKey()).build();
    }
}
