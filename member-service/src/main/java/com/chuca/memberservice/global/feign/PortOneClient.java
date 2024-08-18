package com.chuca.memberservice.global.feign;

import com.chuca.memberservice.global.config.FeignClientConfig;
import com.chuca.memberservice.global.dto.PortOneDto;
import com.chuca.memberservice.global.dto.TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "portone-client",
        url = "https://api.iamport.kr",
        configuration = FeignClientConfig.class
)
public interface PortOneClient {
    @PostMapping("/users/getToken")
    TokenDto.Response getToken(@RequestBody TokenDto.Request request); // access token 발급

    @GetMapping("/vbanks/holder")
    PortOneDto getBankHolder(@RequestHeader("Authorization") String accessToken, @RequestParam("bank_code") String code, @RequestParam("bank_num") String bank); // 계좌 실명 조회
}
