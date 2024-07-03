package com.chuca.gateway.discovery;

import com.chuca.gateway.discovery.eureka.EurekaDiscoveryClient;
import com.chuca.gateway.discovery.kubernetes.KubernetesDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DiscoveryClientConfig {
    // eureka
    @Bean
    @Profile("local")
    public DiscoveryClient eurekaDiscoveryClient() {
        return new EurekaDiscoveryClient();
    }

    // Kubernetes
    @Bean
    @Profile("prod")
    public DiscoveryClient kubernetesDiscoveryClient() {
        return new KubernetesDiscoveryClient();
    }
}
