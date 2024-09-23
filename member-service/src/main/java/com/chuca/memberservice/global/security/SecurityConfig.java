package com.chuca.memberservice.global.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtProvider jwtProvider;
    private final JwtExceptionFilter jwtExceptionFilter;

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Order(0)
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers("/swagger-ui/**", "/swagger/**", "/swagger-resources/**", "/swagger-ui.html",
                        "/configuration/ui",  "/v3/api-docs/**", "/h2-console/**", "/oauth/**",
                        "/member/signup", "/member/check-id", "/member/login", "/member/reissue",
                        "/owner/signup", "/owner/check", "/owner/login", "/owner/reissue",
                        "/member/check-email");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .sessionManagement((sessionManagement) ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilter(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtProvider))
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/auth/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/member/signup")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/member/login")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/member/check-id")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/member/check-email")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/member/reissue")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/owner/signup")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/owner/login")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/owner/reissue")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/owner/check")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/owner/**")).hasAnyRole("OWNER", "ADMIN") // 내부적으로 ROLE_ prefix 자동으로 붙임
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/member/**")).hasAnyRole("USER", "ADMIN") // 내부적으로 ROLE_ prefix 자동으로 붙임
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(new CustomAccessDeniedHandler()) // 커스텀 AccessDeniedHandler 등록 (요청 권한 없을 때 에러 처리)
                )
                .headers(
                        headersConfigurer ->
                                headersConfigurer
                                        .frameOptions(
                                                HeadersConfigurer.FrameOptionsConfig::sameOrigin
                                        )
                )
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}
