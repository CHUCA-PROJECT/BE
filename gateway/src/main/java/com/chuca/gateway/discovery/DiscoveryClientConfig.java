package com.chuca.gateway.discovery;

import com.chuca.gateway.discovery.eureka.EurekaDiscoveryClient;
import com.chuca.gateway.discovery.kubernetes.KubernetesDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DiscoveryClientConfig {
    @Bean
    @Profile("eureka")
    public DiscoveryClient eurekaDiscoveryClient() {
        return new EurekaDiscoveryClient();
    }

    @Bean
    @Profile("kubernetes")
    public DiscoveryClient kubernetesDiscoveryClient() {
        return new KubernetesDiscoveryClient();
    }
}
