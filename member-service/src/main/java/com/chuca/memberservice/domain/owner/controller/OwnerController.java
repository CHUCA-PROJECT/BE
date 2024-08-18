//package com.chuca.memberservice.domain.owner.controller;
//
//import com.chuca.memberservice.domain.owner.dto.BankDto;
//import com.chuca.memberservice.domain.owner.dto.CafeDto;
//import com.chuca.memberservice.domain.owner.dto.LoginDto;
//import com.chuca.memberservice.domain.owner.dto.OwnerDto;
//import com.chuca.memberservice.domain.owner.service.OwnerService;
//import com.chuca.memberservice.global.dto.BusinessDto;
//import com.chuca.memberservice.global.response.BaseResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/owner")
//public class OwnerController {
//    private final OwnerService ownerService;
//
//    // 1. 회원가입 및 입점신청
//    @PostMapping("/signup")
//    public ResponseEntity<BaseResponse<CafeDto.Response>> signup(@RequestBody @Validated OwnerDto.Request request) {
//        return ResponseEntity.ok(BaseResponse.create(ownerService.signup(request)));
//    }
//
//    // 2. 사업자 진위 여부 확인
//    @PostMapping("/check")
//    public ResponseEntity<BaseResponse<Boolean>> checkBusinessNum(@RequestBody @Validated BusinessDto.Request request) {
//        return ResponseEntity.ok(BaseResponse.create(ownerService.checkBusinessNum(request)));
//    }
//
//    // 3. 계좌 실명 조회
//    @PostMapping("/bank")
//    public ResponseEntity<BaseResponse<String>> checkAccountRealName(@RequestBody @Validated BankDto request) {
//        return ResponseEntity.ok(BaseResponse.create(ownerService.checkAccountRealName(request)));
//    }
//
//    // 4. 사장님 로그인
//    @PostMapping("/login")
//    public ResponseEntity<BaseResponse<LoginDto.Response>> login(@RequestBody @Validated LoginDto.Request request) {
//        return ResponseEntity.ok(BaseResponse.create(ownerService.login(request)));
//    }
//}
