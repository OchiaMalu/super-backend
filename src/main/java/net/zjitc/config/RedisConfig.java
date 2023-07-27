package net.zjitc.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RedisConfig
 *
 * @author OchiaMalu
 * @date 2023/07/28
 */
@Configuration
public class RedisConfig {
    /**
     * redisson客户
     *
     * @return {@link RedissonClient}
     */
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        return Redisson.create(config);
    }
}
