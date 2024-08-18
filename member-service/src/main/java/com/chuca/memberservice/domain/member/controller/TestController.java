package com.chuca.memberservice.domain.member.controller;

import com.chuca.memberservice.domain.member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Hello, World!";
    }
}
