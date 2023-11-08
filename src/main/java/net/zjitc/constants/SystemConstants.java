package net.zjitc.constants;

import org.springframework.beans.factory.annotation.Value;

/**
 * 系统常量
 *
 * @author OchiaMalu
 * @date 2023/06/22
 */
public interface SystemConstants {
    /**
     * 页面大小
     */
    long PAGE_SIZE = 8;

    /**
     * 七牛云图片url前缀
     */
    @Value("${super.qiniu.url}")
    String QiNiuUrl = null;

    /**
     * 电子邮件发送邮箱
     */
    String EMAIL_FROM = "linzhehao1108@126.com";

    /**
     * 默认缓存页数
     */
    int DEFAULT_CACHE_PAGE = 5;
}
