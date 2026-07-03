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
public class CustomUserDetailsService implements UserDetailsService{
	private final MemberMapper memberMapper;
	@Override
	public UserDetails loadUserByUsername(String memberId) {
		Member member = memberMapper.findByMemberId(Long.parseLong(memberId));
		if(member == null) {
			throw new UsernameNotFoundException("회원을 찾을 수 없습니다");
		}
		//springsecurity 사용 가능 형태로 변
		return new CustomUserDetails(member);
	}

}
