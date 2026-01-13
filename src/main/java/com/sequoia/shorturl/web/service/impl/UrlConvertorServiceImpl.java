package com.sequoia.shorturl.web.service.impl;

import cn.hutool.bloomfilter.BloomFilter;
import cn.hutool.core.util.StrUtil;
import com.sequoia.shorturl.common.exception.ObjectNotExistException;
import com.sequoia.shorturl.common.server.ShortUrlGenerator;
import com.sequoia.shorturl.web.repository.UrlConvertorRepository;
import com.sequoia.shorturl.web.service.IUrlConvertorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: xxx
 * @Date: 2022/1/3 22:58
 * @Version: 1.0.0
 */
@Service
public class UrlConvertorServiceImpl implements IUrlConvertorService {

    private static final String DUPLICATE = "#";
    @Autowired
    private BloomFilter bloomFilter;

    private final UrlConvertorRepository convertorRepository;

    public UrlConvertorServiceImpl(UrlConvertorRepository convertorRepository) {
        this.convertorRepository = convertorRepository;
    }

    @Override
    public String longUrlToShortUrl(String longUrl) {
        // 生成短链
        String shortUrl = ShortUrlGenerator.generate(longUrl);
        return saveUrlMapping(shortUrl, longUrl, longUrl);
    }

    @Override
    public String getLongUrlByShortUrl(String shortUrl) {
        // 判断短链是否不存在
        if (bloomFilter.contains(shortUrl)) {
            return convertorRepository.getLongUrlByShortUrl(shortUrl);
        } else {
            throw new ObjectNotExistException("请求的url不存在,请核对后再试!");
        }
    }

    private String saveUrlMapping(String shortUrl, String longUrl, String conflictUrl) {
        // 在过滤器中查找短url是否存在
        boolean existsInBloom = bloomFilter.contains(shortUrl);
        if (existsInBloom) {
             String existingLongUrl = convertorRepository.getLongUrlByShortUrl(shortUrl);
             if (longUrl.equals(existingLongUrl)) {
                 return shortUrl;
             }
             // Collision detected (Bloom says yes, but actual value is different or not found)
             // If existingLongUrl is empty, it might be a false positive from Bloom filter,
             // so we should attempt to save.
             if (StrUtil.isEmpty(existingLongUrl)) {
                 // Try to save
                 if (convertorRepository.saveIfAbsent(shortUrl, longUrl)) {
                     bloomFilter.add(shortUrl);
                     return shortUrl;
                 } else {
                     // Check again if we lost the race to the same longUrl
                     if (longUrl.equals(convertorRepository.getLongUrlByShortUrl(shortUrl))) {
                         return shortUrl;
                     }
                 }
             }
        } else {
            // Not in bloom filter, try to save
            if (convertorRepository.saveIfAbsent(shortUrl, longUrl)) {
                bloomFilter.add(shortUrl);
                return shortUrl;
            } else {
                 // Check if we lost the race to the same longUrl
                 if (longUrl.equals(convertorRepository.getLongUrlByShortUrl(shortUrl))) {
                     return shortUrl;
                 }
                 // If not equal, it means collision or someone else took the spot
            }
        }

        // Conflict resolution
        conflictUrl = conflictUrl + DUPLICATE;
        shortUrl = ShortUrlGenerator.generate(conflictUrl);
        return saveUrlMapping(shortUrl, longUrl, conflictUrl);
    }
}
