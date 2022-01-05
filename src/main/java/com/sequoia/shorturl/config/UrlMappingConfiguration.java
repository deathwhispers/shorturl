package com.sequoia.shorturl.config;

import com.sequoia.shorturl.web.repository.UrlConvertorMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author: yanggj
 * @Description: urlMap配置类
 * @Date: 2022/1/5 22:14
 * @Version: 1.0.0
 */
@Configuration
public class UrlMappingConfiguration {

    @Resource
    private ScheduleProps scheduleProps;

    @Bean
    public UrlConvertorMapping getUrlConvertorMapping() {
        return new UrlConvertorMapping(scheduleProps.getTtl(), scheduleProps.getPeriod(), scheduleProps.getTimeUnit());
    }
}
