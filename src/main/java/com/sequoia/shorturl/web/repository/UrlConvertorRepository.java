package com.sequoia.shorturl.web.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @Author: yanggj
 * @Description: TODO
 * @Date: 2022/1/4 22:48
 * @Version: 1.0.0
 */
@Repository
public class UrlConvertorRepository {
    private static final Logger logger = LoggerFactory.getLogger(UrlConvertorRepository.class);

    @Resource
    private UrlConvertorMapping urlConvertorMapping;

    public String getLongUrlByShortUrl(String shortUrl) {
        return urlConvertorMapping.get(shortUrl);
    }

    public void save(String shortUrl, String longUrl) {
        urlConvertorMapping.put(shortUrl,longUrl);
    }
}
