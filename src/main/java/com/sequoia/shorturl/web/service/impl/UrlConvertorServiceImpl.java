package com.sequoia.shorturl.web.service.impl;

import cn.hutool.bloomfilter.BloomFilter;
import com.sequoia.shorturl.common.ObjectNotExistException;
import com.sequoia.shorturl.common.server.ShortUrlGenerator;
import com.sequoia.shorturl.web.repository.UrlConvertorRepository;
import com.sequoia.shorturl.web.service.IUrlConvertorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: xxx
 * @Date: 2022/1/3 22:58
 * @Version: 1.0.0
 */
@Service
public class UrlConvertorServiceImpl implements IUrlConvertorService {

    @Resource
    private BloomFilter bloomFilter;

    private final UrlConvertorRepository convertorRepository;

    public UrlConvertorServiceImpl(UrlConvertorRepository convertorRepository) {
        this.convertorRepository = convertorRepository;
    }

    @Override
    public String longUrlToShortUrl(String longUrl) {
        // 生成短链
        String shortUrl = ShortUrlGenerator.generate(longUrl);
        return saveUrlMapping(shortUrl, longUrl);
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

    private String saveUrlMapping(String shortUrl, String longUrl) {
        // 在过滤器中查找短url是否存在
        if (bloomFilter.contains(shortUrl)) {
            // 存在则直接返回
            if (longUrl.equals(convertorRepository.getLongUrlByShortUrl(shortUrl))) {
                return shortUrl;
            }
        }
        // 库中不存在,则保存键值对,并添加到bloomFilter
        if (convertorRepository.getLongUrlByShortUrl(shortUrl) == null) {
            synchronized (shortUrl.intern()) {
                if (convertorRepository.getLongUrlByShortUrl(shortUrl) == null) {
                    convertorRepository.save(shortUrl, longUrl);
                    bloomFilter.add(shortUrl);
                    return shortUrl;
                }
            }
        }
        // 存在但不相等,说明出现了hash冲突,则在长串后拼接 DUPLICATE再此生成

        shortUrl =ShortUrlGenerator.generate(longUrl);

        return shortUrl;
    }
}
