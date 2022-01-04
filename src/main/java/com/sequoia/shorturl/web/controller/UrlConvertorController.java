package com.sequoia.shorturl.web.controller;

import cn.hutool.core.util.StrUtil;
import com.sequoia.shorturl.common.ApiResult;
import com.sequoia.shorturl.web.service.IUrlConvertorService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xxx
 * @Description: TODO
 * @Date: 2022/1/3 22:57
 * @Version: 1.0.0
 */
@RestController("/api/v0")
public class UrlConvertorController {
    private static final Logger logger = LoggerFactory.getLogger(UrlConvertorController.class);

    private final IUrlConvertorService urlConvertorService;

    public UrlConvertorController(IUrlConvertorService urlConvertorService) {
        this.urlConvertorService = urlConvertorService;
    }

    @PostMapping("/to-short-url/{url}")
    @ApiOperation("")
    public ApiResult<String> longUrlToShortUrl(@PathVariable String url) {
        if (StrUtil.isBlank(url)) {
            return ApiResult.create(400, "url不能为空", url);
        }
        String shortUrl = urlConvertorService.longUrlToShortUrl(url);
        return ApiResult.ok(shortUrl);
    }

    @GetMapping("/{shortUrl}")
    public ApiResult<String> getLongUrlByShortUrl(@PathVariable String shortUrl) {
        String longUrl = urlConvertorService.getLongUrlByShortUrl(shortUrl);
        if (StrUtil.isBlank(longUrl)) {
            return ApiResult.create(500, "不存在该链接,请核查!", longUrl);
        }
        return ApiResult.ok(longUrl);
    }


}
