package com.chuca.memberservice.global.feign;

import com.chuca.memberservice.global.config.FeignClientConfig;
import com.chuca.memberservice.global.dto.BusinessDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "businessman-check-validate-client",
        url = "https://api.odcloud.kr/api/nts-businessman/v1",
        configuration = FeignClientConfig.class
)
public interface BusinessManClient {
    @PostMapping("/validate")
    BusinessDto.Response checkValidate(@RequestParam String serviceKey, @RequestBody BusinessDto.Request businesses);
}
