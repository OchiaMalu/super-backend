package net.zjitc.utils;

import java.util.Random;

/**
 * 验证码生成工具
 *
 * @author OchiaMalu
 * @date 2023/06/22
 */
public final class ValidateCodeUtils {
    private ValidateCodeUtils() {
    }

    /**
     * 生成验证代码
     *
     * @param length 长度
     * @return {@link Integer}
     */
    public static Integer generateValidateCode(int length) {
        int code;
        if (length == 4) {
            code = new Random().nextInt(9999); //生成随机数，最大为9999
            if (code < 1000) {
                code = code + 1000; //保证随机数为4位数字
            }
        } else if (length == 6) {
            code = new Random().nextInt(999999); //生成随机数，最大为999999
            if (code < 100000) {
                code = code + 100000; //保证随机数为6位数字
            }
        } else {
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }
}
