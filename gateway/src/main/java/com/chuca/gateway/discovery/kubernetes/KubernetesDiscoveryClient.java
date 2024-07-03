package com.chuca.gateway.discovery.kubernetes;

import com.chuca.gateway.discovery.DiscoveryClient;
import org.springframework.context.annotation.Profile;

@Profile("prod")
public class KubernetesDiscoveryClient implements DiscoveryClient {
    @Override
    public void registerService() {
        // Kubernetes 서비스 등록 로직
    }

    @Override
    public void discoverService() {
        // Kubernetes 서비스 디스커버리 로직
    }
}
