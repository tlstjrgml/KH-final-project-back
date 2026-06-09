package com.moa.backend.member.service;

import org.springframework.stereotype.Service;

import com.moa.backend.member.model.mapper.MemberMapper;
import com.moa.backend.member.model.vo.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberMapper mapper;

	public int insertMember(Member m) {
		return mapper.insertMember(m);
	}
	
	public Member findByKaKaoId(Long kakaoId) {
		return mapper.findByKaKaoId(kakaoId);
	}

}
