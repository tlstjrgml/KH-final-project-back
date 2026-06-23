package com.moa.backend.member.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.moa.backend.common.config.jwt.JwtProvider;
import com.moa.backend.member.model.vo.Member;
//카카오 로그인 성공 후 jwt 토큰 발급 및 클라이언트로 전달하는 핸들러, securityconfig의 succeshandler로 등록되어 로그인 성공 시 자동 호출 
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final MemberService mService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
    //인증 객체에서 카카오 사용자 정보 추출 
    OAuth2User oAuthUser = (OAuth2User) authentication.getPrincipal();
    Long kakaoId = (Long) oAuthUser.getAttributes().get("id");
    Member member = mService.findByKakaoId(kakaoId);
    //카카오아이디로 데이터베이스에서 회원 조회 
    Long memberId = member.getMemberId();
    //jwt토큰 발급 
    String token = jwtProvider.generateToken(memberId, member.getIsAdmin(), member.getNickname());
    //토큰을 쿼리스트링에 담다 리다이렉
    response.sendRedirect("http://localhost:5173/?token=" + token);
    }
}