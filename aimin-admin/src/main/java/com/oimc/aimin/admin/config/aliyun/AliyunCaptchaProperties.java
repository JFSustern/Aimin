package com.oimc.aimin.admin.config.aliyun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "aliyun.captcha")
public class AliyunCaptchaProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private Integer connectTimeout;
    private Integer readTimeout;
    private String sceneId;
    private Boolean failOpen;
}
