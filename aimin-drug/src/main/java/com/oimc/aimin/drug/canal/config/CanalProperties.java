package com.oimc.aimin.drug.canal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
 *
 * @author 渣哥
 */
@Data
@ConfigurationProperties(prefix = "canal")
@Component
public class CanalProperties {
    private String destination;
    private Server server;
    private Long initialDelay;
    private Long fixedDelay;
    private String subscribe;
}
