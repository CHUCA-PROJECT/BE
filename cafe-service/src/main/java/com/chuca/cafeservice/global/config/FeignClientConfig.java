package com.chuca.cafeservice.global.config;

import com.chuca.cafeservice.CafeServiceApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = CafeServiceApplication.class)
public class FeignClientConfig {
}

