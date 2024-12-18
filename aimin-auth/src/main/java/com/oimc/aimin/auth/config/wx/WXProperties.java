package com.oimc.aimin.auth.config.wx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "wx")
@Component
public class WXProperties {
    private String appid;
    private String secret;
}
