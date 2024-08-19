//package com.chuca.memberservice.domain.member.service.oauth;
//
//import com.chuca.memberservice.domain.member.domain.constant.MemberProvider;
//import com.chuca.memberservice.domain.member.dto.OAuthLoginDto;
//import com.chuca.memberservice.domain.member.domain.entity.Member;
//import com.chuca.memberservice.domain.member.domain.repository.MemberRepository;
//import com.chuca.memberservice.global.dto.NaverTokenDto;
//import com.chuca.memberservice.global.dto.NaverUserDto;
//import com.chuca.memberservice.global.exception.UnauthorizedException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class NaverOauthService {
//    @Value("${security.oauth2.client.provider.naver.authorization-uri}")
//    private String NAVER_AUTHORIZATINO_URI;
//
//    @Value("${security.oauth2.client.provider.naver.token-uri}")
//    private String NAVER_TOKEN_URI;
//
//    @Value("${security.oauth2.client.provider.naver.user-info-uri}")
//    private String NAVER_USER_INFO_URI;
//
//    @Value("${security.oauth2.client.registration.naver.redirect-uri}")
//    private String NAVER_REDIRECT_URI;
//
//    @Value("${security.oauth2.client.registration.naver.client-id}")
//    private String NAVER_CLIENT_ID;
//
//    @Value("${security.oauth2.client.registration.naver.client-secret}")
//    private String NAVER_CLIENT_SECRET;
//
//    private final ObjectMapper objectMapper;
//    private final HttpServletResponse response;
//    private final MemberRepository memberRepository;
//
//    // CSS 공격 방지를 위한 상태 토큰값
//    public String generateRandomString() {
//        return UUID.randomUUID().toString();
//    }
//
//    public String getAuthorizatioUri() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(NAVER_AUTHORIZATINO_URI);
//        sb.append("?response_type=" + "code");
//        sb.append("&client_id=" + NAVER_CLIENT_ID);
//        sb.append("&redirect_uri=" + NAVER_REDIRECT_URI);
//        sb.append("&state=" + generateRandomString());
//
//        return sb.toString();
//    }
//
//    // 1. 네이버 로그인 (사용자 로그인 페이지 제공 단계)
//    public void request() throws IOException {
//        String redirectURL = getAuthorizatioUri();
//        response.sendRedirect(redirectURL);
//    }
//
//    // 2. 네이버 로그인 (사용자 정보 조회)
//    @Transactional
//    public OAuthLoginDto.Response login(String code, String state) throws JsonProcessingException {
//        // 1단계 : 네이버 액세스 토큰 발급
//        ResponseEntity<String> tokenResult = requestAccessToken(code, state);
//        String accessToken = getAccessToken(tokenResult);
//
//        // 2단계 : 액세스 토큰으로 유저 정보 조회
//        ResponseEntity<String> userInfoResult = requestUserInfo(accessToken);
//        NaverUserDto naverUserDto = getUserInfo(userInfoResult);
//
//        // 3단계 : DB에 없는 회원이면 회원가입 + 액세스/리프레시 토큰 발급
//        String email = naverUserDto.getResponse().getEmail();
//        Member getMember = memberRepository.findByEmail(email);
//        if(getMember == null){
//            Member member = Member.builder()
//                    .email(email)
//                    .phone(naverUserDto.getResponse().getMobile())
//                    .nickname(naverUserDto.getResponse().getNickname())
//                    .profileImage(naverUserDto.getResponse().getProfile_image())
//                    .provider(MemberProvider.NAVER)
//                    .build();
//            getMember = memberRepository.save(member);
//        }
//
//        return new OAuthLoginDto.Response("accessToken", "refreshToken"); // 임시
//    }
//
//    // 네이버 액세스 토큰 발급
//    public ResponseEntity<String> requestAccessToken(String code, String state) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", NAVER_CLIENT_ID);
//        params.add("client_secret", NAVER_CLIENT_SECRET);
//        params.add("code", code);
//        params.add("state", state);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//        ResponseEntity<String> response = restTemplate.exchange(NAVER_TOKEN_URI, HttpMethod.POST, request, String.class);
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return response;
//        }
//        throw new UnauthorizedException("네이버 액세스 토큰 발급에 실패했습니다.", HttpStatus.UNAUTHORIZED);
//    }
//
//    // 네이버 액세스 토큰 추출
//    public String getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
//        NaverTokenDto naverTokenDto = objectMapper.readValue(response.getBody(), NaverTokenDto.class);
//        return naverTokenDto.getAccess_token();
//    }
//
//    // 네이버 유저 정보 조회
//    public ResponseEntity<String> requestUserInfo(String accessToken) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        // 요청 헤더에 accessToken 담기
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//
//        // HttpEntity를 생성해, 헤더를 담아 restTemplate으로 네이버와 통신
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
//        ResponseEntity<String> response = restTemplate.exchange(NAVER_USER_INFO_URI, HttpMethod.POST, request, String.class);
//        return response;
//    }
//
//    // 네이버 유저 정보 추출
//    public NaverUserDto getUserInfo(ResponseEntity<String> response) throws JsonProcessingException {
//        NaverUserDto naverUserDto = objectMapper.readValue(response.getBody(), NaverUserDto.class);
//        return naverUserDto;
//    }
//}
