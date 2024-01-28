package net.zjitc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 应用程序
 *
 * @author OchiaMalu
 * @date 2024/01/25
 */
@SpringBootApplication
@EnableRedisHttpSession
@EnableAspectJAutoProxy
public class SuperApplication {
    protected SuperApplication() {
    }

    /**
     * 程序入口
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(SuperApplication.class, args);
    }
}

