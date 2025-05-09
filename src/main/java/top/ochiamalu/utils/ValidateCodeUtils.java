package top.ochiamalu.utils;

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
     * @return {@link String}
     */
    public static String generateValidateCode(long digits) {
        int min = (int) Math.pow(10, digits - 1);
        int max = (int) Math.pow(10, digits) - 1;

        Random random = new Random();
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }
}
