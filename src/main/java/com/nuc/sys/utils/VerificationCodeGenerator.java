package com.nuc.sys.utils;

import java.util.Random;

public class VerificationCodeGenerator {
    
    // 生成指定长度的随机验证码
    public static String generateVerificationCode(int length) {
        // 定义验证码字符集合
        String characters = "0123456789";
        // 创建随机对象
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        // 循环生成指定长度的验证码
        for (int i = 0; i < length; i++) {
            // 从字符集合中随机选择一个字符
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            // 将随机选择的字符添加到验证码中
            code.append(randomChar);
        }
        // 返回生成的验证码
        return code.toString();
    }
}
