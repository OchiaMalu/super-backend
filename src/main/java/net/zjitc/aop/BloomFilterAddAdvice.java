package net.zjitc.aop;


import cn.hutool.bloomfilter.BloomFilter;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static net.zjitc.constants.BloomFilterConstants.*;

/**
 * @author OchiaMalu
 */
@Component
@Aspect
@ConditionalOnProperty(prefix = "super", name = "enable-bloom-filter", havingValue = "true")
@Log4j2
public class BloomFilterAddAdvice {

    @Resource
    private BloomFilter bloomFilter;

    @After("execution(* net.zjitc.service.impl.UserServiceImpl.afterInsertUser(..))")
    public void afterInsertUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("add userId " + args[1] + " to BloomFilter");
        bloomFilter.add(USER_BLOOM_PREFIX + args[1]);
    }

    @AfterReturning(value = "execution(long net.zjitc.service.impl.TeamServiceImpl.addTeam(..))", returning = "ret")
    public void afterAddTeam(Object ret) {
        log.info("add teamId " + ret + " to BloomFilter");
        bloomFilter.add(TEAM_BLOOM_PREFIX + ret);
    }

    @AfterReturning(value = "execution(* net.zjitc.service.impl.BlogServiceImpl.addBlog(..))", returning = "ret")
    public void afterAddBlog(Object ret) {
        log.info("add blogId " + ret + " to BloomFilter");
        bloomFilter.add(BLOG_BLOOM_PREFIX + ret);
    }
}
