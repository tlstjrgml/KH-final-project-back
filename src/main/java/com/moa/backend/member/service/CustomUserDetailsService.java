package com.moa.backend.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moa.backend.common.config.jwt.CustomUserDetails;
import com.moa.backend.member.model.mapper.MemberMapper;
import com.moa.backend.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
//spring security가 인증 시 사용자 정보 로딩
//jwtfilter에서 토큰 바싱 후 memberId로 회원 조회 때 사
public class CustomUserDetailsService implements UserDetailsService{
	private final MemberMapper memberMapper;
	/**/
	@Override
	public UserDetails loadUserByUsername(String memberId) {
		//String으로 받은 memberId를 long으로 변환 후 DB조회 
		Member member = memberMapper.findByMemberId(Long.parseLong(memberId));
		if(member == null) {
			throw new UsernameNotFoundException("회원을 찾을 수 없습니다");
		}
		//springsecurity 사용 가능 형태로 변
		return new CustomUserDetails(member);
	}

}
