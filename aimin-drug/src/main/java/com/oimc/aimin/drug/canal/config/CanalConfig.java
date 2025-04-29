package com.oimc.aimin.drug.canal.config;


import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetSocketAddress;

/*
 *
 * @author 渣哥
 */
@Configuration
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
public class CanalConfig {

    private final CanalProperties canalProperties;

    @Bean
    public CanalConnector newSingleConnector() {
        return CanalConnectors.newSingleConnector(new InetSocketAddress(canalProperties.getServer().getIp(),
                canalProperties.getServer().getPort()), canalProperties.getDestination(),  "canal", "canal");
    }

}
