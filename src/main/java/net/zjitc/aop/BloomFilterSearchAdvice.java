package net.zjitc.aop;


import cn.hutool.bloomfilter.BloomFilter;
import lombok.extern.log4j.Log4j2;
import net.zjitc.common.ErrorCode;
import net.zjitc.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static net.zjitc.constants.BloomFilterConstants.BLOG_BLOOM_PREFIX;
import static net.zjitc.constants.BloomFilterConstants.TEAM_BLOOM_PREFIX;
import static net.zjitc.constants.BloomFilterConstants.USER_BLOOM_PREFIX;

/**
 * 布隆过滤器搜索通知
 *
 * @author OchiaMalu
 * @date 2024/01/25
 */
@Component
@Aspect
@ConditionalOnProperty(prefix = "super", name = "enable-bloom-filter", havingValue = "true")
@Log4j2
public class BloomFilterSearchAdvice {
    @Resource
    private BloomFilter bloomFilter;


    /**
     * 发现用户通过id
     *
     * @param joinPoint 连接点
     */
    @Before("execution(* net.zjitc.controller.UserController.getUserById(..))")
    public void findUserById(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        boolean contains = bloomFilter.contains(USER_BLOOM_PREFIX + args[0]);
        if (!contains) {
            log.error("没有在 BloomFilter 中找到该 userId");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有找到该用户");
        }
    }

    /**
     * 发现团队通过id
     *
     * @param joinPoint 连接点
     */
    @Before("execution(* net.zjitc.controller.TeamController.getTeamById(..))")
    public void findTeamById(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        boolean contains = bloomFilter.contains(TEAM_BLOOM_PREFIX + args[0]);
        if (!contains) {
            log.error("没有在 BloomFilter 中找到该 teamId");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有找到该队伍");
        }
    }

    /**
     * 发现团队成员通过id
     *
     * @param joinPoint 连接点
     */
    @Before("execution(* net.zjitc.controller.TeamController.getTeamMemberById(..))")
    public void findTeamMemberById(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        boolean contains = bloomFilter.contains(TEAM_BLOOM_PREFIX + args[0]);
        if (!contains) {
            log.error("没有在 BloomFilter 中找到该 teamId");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有找到该队伍");
        }
    }

    /**
     * 发现博客通过id
     *
     * @param joinPoint 连接点
     */
    @Before("execution(* net.zjitc.controller.BlogController.getBlogById(..))")
    public void findBlogById(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        boolean contains = bloomFilter.contains(BLOG_BLOOM_PREFIX + args[0]);
        if (!contains) {
            log.error("没有在 BloomFilter 中找到该 blogId");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有找到该博文");
        }
    }
}
