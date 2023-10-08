package com.reine.backend.utils;

import java.util.Random;

/**
 * @author reine
 */
public class RandomUtils {

    /**
     * 生成随机字符串
     * @param length 长度
     * @return str
     */
    public static String getRandomStr(int length){
        StringBuilder filename = new StringBuilder();
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(str.length());
            filename.append(str.charAt(index));
        }
        return filename.toString();
    }

}
