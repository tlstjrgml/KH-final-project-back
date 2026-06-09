package com.moa.backend.member.service;

import org.springframework.stereotype.Service;

import com.moa.backend.member.model.mapper.MemberMapper;
import com.moa.backend.member.model.vo.Member;
import com.moa.backend.member.model.vo.MemberDetail;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberMapper mapper;

	public int insertMember(Member m) {
		return mapper.insertMember(m);
	}
	
	public Member findByKakaoId(Long kakaoId) {
		return mapper.findByKakaoId(kakaoId);
	}
	
	public int insertMemberDetail(MemberDetail MD) {
		return mapper.insertMemberDetail(MD);
	}
}
