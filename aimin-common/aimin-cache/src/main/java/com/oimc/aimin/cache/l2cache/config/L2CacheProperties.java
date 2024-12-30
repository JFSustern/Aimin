package com.oimc.aimin.cache.l2cache.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Data
@ConfigurationProperties(prefix = "l2cache")
public class L2CacheProperties {

    private int initialCapacity;
    private int maximumSize;
    private int expireAfterWrite;
    private String timeUnit;
    private TimeUnit expireAfterWriteTimeUnit;

    @PostConstruct
    public void init() {
        if (timeUnit != null) {
            try {
                this.expireAfterWriteTimeUnit = TimeUnit.valueOf(timeUnit.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid timeUnit value: " + timeUnit);
            }
        } else {
            this.expireAfterWriteTimeUnit = TimeUnit.SECONDS; // 设置默认值
        }
    }
}
