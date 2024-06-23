package com.chuca.gateway.discovery.eureka;

import com.chuca.gateway.discovery.DiscoveryClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class EurekaDiscoveryClient implements DiscoveryClient {
    @Override
    public void registerService() {
        // Eureka 서비스 등록 로직
    }

    @Override
    public void discoverService() {
        // Eureka 서비스 디스커버리 로직
    }
}
