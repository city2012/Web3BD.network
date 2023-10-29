package com.bfit.recommand.common.util;

import java.security.SecureRandom;

public class RandomUtils {

    private final static SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1);

    public static String generateRandomString() {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(5);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static String genSnowflakeWithSuffix(String suffix){
        return suffix + generateRandomString();
    }

    public static Long generateSnowflake(){
        // 生成唯一ID
        return idGenerator.generateId();
    }

}
