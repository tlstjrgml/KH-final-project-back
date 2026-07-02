package com.moa.backend.common.config.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	// application.properties에서 주입받는 서명용 비밀키
	@Value("${jwt.secret}")
	private String secretKey;
	// application.properties에서 주입받는 만료 시간
	@Value("${jwt.expiration}")
	private long expiration;

	// 토큰생성:로그인 성공 시 memberId를 클레임에 담아 jwt 발급
	public String generateToken(Long memberId, String isAdmin, String nickname) {
		return Jwts.builder().claim("memberId", memberId) // 커스텀 클레임: 회원 아이디
				.claim("isAdmin", isAdmin)
				.claim("nickname", nickname)
				.issuedAt(new Date()) // iat: 발급시간
				.expiration(new Date(System.currentTimeMillis() + expiration)) // exp:만료시간
				.signWith(Keys.hmacShaKeyFor(secretKey.getBytes())) // hs256 서명
				.compact();
	}

	// 토큰 유효성 검증 : 서명 불일치 or 만료시 false
	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 회원 조회 : 토큰에서 memberId추출, 검증된 토큰의 페이로드에서 memberId클레임 파싱
	public Long getMemberId(String token) {
		Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
				.parseSignedClaims(token).getPayload();
		return claims.get("memberId", Long.class);
	}
}
