package com.chuca.memberservice.global.config;

import com.chuca.memberservice.MemberServiceApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = MemberServiceApplication.class)
public class FeignClientConfig {
}
