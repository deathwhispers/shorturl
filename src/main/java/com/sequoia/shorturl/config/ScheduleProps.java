package com.sequoia.shorturl.config;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yanggj
 * @Description: 定时任务参数
 * @Date: 2022/1/5 22:34
 * @Version: 1.0.0
 */
public class ScheduleProps {

    private long ttl;
    private long period;
    private TimeUnit timeUnit;

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}
