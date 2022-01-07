package com.sequoia.shorturl.common.server;

import cn.hutool.core.codec.Base62;

/**
 * @Author: xxx
 * @Description: 短链发号器
 * @Date: 2022/1/4 22:11
 * @Version: 1.0.0
 */
public class ShortUrlGenerator {

    public static final String space = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int spaceLength = space.length();

    public static String encode(Long sequence) {
        String base62 = "";
        do {
            int mod = (int)(sequence % spaceLength);
            base62 = space.charAt(mod) + base62;
            sequence = sequence / spaceLength;
        } while(sequence > 0);

        return base62;
    }

    /**
     * 生成短url
     *
     * @param longUrl 源 长url
     * @return shortUrl
     */
    public static String generate(String longUrl) {
        //


        // 采用默认U8字符集
        return Base62.encode(longUrl);
    }

}
