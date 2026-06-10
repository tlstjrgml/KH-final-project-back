package com.moa.backend.common.config.jwt;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.moa.backend.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

//springsecurity가 인증에 사용하는 사용자 정보 클래스, member객체를 userDetails인터페이스 구현 
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails{
	private final Member member;

	//회원권한 반환: is_admin 값에 따라 ROLE_ADMIN, ROLD_USER 
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String role = member.getIsAdmin().equals("Y") ? "ROLE_ADMIN" : "ROLE_USER";
		return List.of(new SimpleGrantedAuthority(role));
	}
	
	//비밀번호 반환 
	@Override
	public  String getPassword() {
		return member.getPassword();
	}

	//사용자 식별자 반환(username자리에 memberId를 String으로 변환해서 사용 
	@Override
	public String getUsername() {
		return String.valueOf(member.getMemberId());
	}
	
	//계정 만료 여부(true=만료되지 않음)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	//비밀번호 만료 여부(true=만료되지 않음) 
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	//계정 활성화 여부(true=활성화)
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
	
}
