//package com.chuca.memberservice.domain.member.controller;
//
//import com.chuca.memberservice.domain.member.dto.OAuthLoginDto;
//import com.chuca.memberservice.domain.member.service.oauth.KakaoOauthService;
//import com.chuca.memberservice.domain.member.service.oauth.NaverOauthService;
//import com.chuca.memberservice.global.response.BaseResponse;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/oauth")
//@RequiredArgsConstructor
//public class OAuthController {
//
//    private final NaverOauthService naverOauthService;
//    private final KakaoOauthService kakaoOauthService;
//
//    // 1. 네이버 로그인 (사용자 로그인 페이지 제공 단계)
//    @GetMapping("/{socialType}")
//    public void naverLogin(@PathVariable("socialType") String provider) throws IOException {
//        naverOauthService.request();
//    }
//
//    // 2. 네이버 로그인 (사용자 정보 받기)
//    @GetMapping("/{socialType}/callback")
//    public ResponseEntity<BaseResponse<OAuthLoginDto.Response>> callback(
//            @PathVariable("socialType") String provider,
//            @RequestParam String code,
//            @RequestParam String state) throws JsonProcessingException {
//        return ResponseEntity.ok(BaseResponse.create(naverOauthService.login(code, state)));
//    }
//
//    // 3. 카카오 로그인 및 회원가입 (기획 로직 수정 중이라 추후에 약관 관련한 처리해야하고, 사업자 정보 발급하면 전화번호도 받아오기)
//    @PostMapping("/{socialType}")
//    public ResponseEntity<BaseResponse<OAuthLoginDto.Response>> kakaoLogin(@PathVariable("socialType") String provider, @RequestBody OAuthLoginDto.Request request) {
//        return ResponseEntity.ok(BaseResponse.create(kakaoOauthService.kakaoLogin(request)));
//    }
//}
