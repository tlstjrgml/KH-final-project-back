package com.moa.backend.common.config.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.moa.backend.member.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // header에서 토큰 추출
        String token = request.getHeader("Authorization");

        // 토큰 없으면 통과
        if (token == null || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 제거
        String jwt = token.substring(7);

        // token 검증
        if (jwtProvider.validateToken(jwt)) {
            // memberId 파싱
            Long memberId = jwtProvider.getMemberId(jwt);

            // DB에서 회원 조회
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(String.valueOf(memberId));

            // 인증 객체 생성 및 SecurityContextHolder에 저장
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터
        filterChain.doFilter(request, response);
    }
}