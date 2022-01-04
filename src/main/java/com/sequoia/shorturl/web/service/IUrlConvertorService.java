package com.sequoia.shorturl.web.service;

/**
 * @Author: xxx
 * @Description: TODO
 * @Date: 2022/1/3 22:58
 * @Version: 1.0.0
 */
public interface IUrlConvertorService {
    /**
     * 长链接转短链接
     * @param longUrl 待转换的长链接
     * @return
     */
    String longUrlToShortUrl(String longUrl);

    String getLongUrlByShortUrl(String shortUrl);
}
