package com.moa.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import lombok.RequiredArgsConstructor;
import com.moa.backend.member.service.CustomOAuth2UserService;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CustomOAuth2UserService customOAuth2UserService;
	
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) {
		http.authorizeHttpRequests(auth -> auth
			.anyRequest().permitAll() //모든 요청 허
		)
		.csrf(csrf -> csrf.disable()) //CSRF 비활성
		.oauth2Login(oauth2 -> oauth2
				.defaultSuccessUrl("/") //로그인 성공 후 이동 URL(메인으로 이동)
				.userInfoEndpoint(userInfo -> userInfo
						.userService(customOAuth2UserService) // 사용자 정보 처리 서비스
			)
		);
		return http.build();
	}
	
	
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
