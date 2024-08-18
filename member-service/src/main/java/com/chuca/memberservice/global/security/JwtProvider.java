package com.chuca.memberservice.global.security;

import com.chuca.memberservice.domain.member.repository.MemberRepository;
import com.chuca.memberservice.domain.member.service.MemberDetailServiceImpl;
import com.chuca.memberservice.domain.member.dto.LoginDto;
import com.chuca.memberservice.domain.owner.repository.OwnerRepository;
import com.chuca.memberservice.global.exception.BadRequestException;
import com.chuca.memberservice.global.util.RedisService;
import io.jsonwebtoken.*;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    // 일반 유저
    private final MemberDetailServiceImpl memberDetailService;
    private final MemberRepository memberRepository;
    private final OwnerRepository ownerRepository;

    private final RedisService redisService;
    private final Long tokenValidTime = 1000L * 60 * 60; // 1h
    private final Long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 7; // 7d


    // access token 생성
    public String encodeJwtToken(Long memberId) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("chuca")
                .setIssuedAt(now)
                .setSubject(memberId.toString())
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .claim("memberId", memberId)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    // refresh token 생성
    public String encodeJwtRefreshToken(Long memberId) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setSubject(memberId.toString())
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .claim("memberId", memberId)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    // 토큰 재발급 (access token, refresh token 둘 다 재발급)
    public LoginDto.Response regenerateToken(String token) {
        Long memberId = getMemberIdFromJwtToken(token);
        String storedRefreshToken = redisService.getValues(String.valueOf(memberId));

        if(storedRefreshToken == null || !storedRefreshToken.equals(token))
            throw new BadRequestException("RefreshToken이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);

        String newAccessToken = encodeJwtToken(memberId); // access token 재발급
        String newRefreshToken = encodeJwtRefreshToken(memberId); // refresh token 재발급
        storeJwtRefreshToken(memberId, newRefreshToken); // redis에 refresh 저장

        return new LoginDto.Response(newAccessToken, newRefreshToken);
    }

    // JWT 토큰으로부터 memberId 추출
    public Long getMemberIdFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch(Exception e) {
            throw new io.jsonwebtoken.JwtException(e.getMessage());
        }
    }


    // Autorization : Bearer에서 token 추출 (refreshToken, accessToken 포함)
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER);
        if(bearerToken == null)
            throw new NullPointerException();
        else if(StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 + 만료일자 확인 (만료 여부만 확인. 에러 발생 x)
    public boolean validateToken(String token) {
        Date now = new Date();

        try{
            // 주어진 토큰을 파싱하고 검증.
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token);

            Long memberId = claims.getBody().get("memberId", Long.class);
            log.info("validateToken ------- memberId : " + memberId);

            /*
             * 이미 로그아웃 or 하이재킹 당한 토큰으로 로그인 시도중인지 체크
             * 1. 이미 로그아웃한 경우 <access, memberId>를 redis에 저장
             * 2. redis에 저장된 value가 memberId와 동일 -> 토큰 탈취
             */
            String expiredAt = redisService.getValues(token);
            if(expiredAt != null && expiredAt.equals(String.valueOf(memberId))) {
                log.info("Hijacking!!!!!!!");
                return false;
            }

            return !claims.getBody().getExpiration().before(new Date(now.getTime()));
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // JWT 토큰 인증 정보 조회 (토큰 복호화)
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = memberDetailService.loadUserByUsername(this.getMemberIdFromJwtToken(token).toString());
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    // refresh token redis에 저장
    public void storeJwtRefreshToken(Long memberId, String token) {
        redisService.setValues(String.valueOf(memberId), token, Duration.ofSeconds(refreshTokenValidTime));
    }

    // 로그아웃을 위한 토큰 만료
    public void expireToken(Long memberId, String token) {
        long expiredAccessTokenTime = getExpiredTime(token).getTime() - new Date().getTime();
        // 1. Redis에 액세스 토큰값을 key로 가지는 memberId 값 저장 (블랙리스트 처리)
        redisService.setValues(token,String.valueOf(memberId), Duration.ofSeconds(expiredAccessTokenTime));
        // 2. Redis에 저장된 refreshToken 삭제
        redisService.deleteValues(String.valueOf(memberId));
    }

    // 토큰 유효 시간 계산
    public Date getExpiredTime(String token){
        return Jwts
                .parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
