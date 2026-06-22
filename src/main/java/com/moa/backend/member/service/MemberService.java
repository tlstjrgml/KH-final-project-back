package com.moa.backend.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moa.backend.board.dto.BoardResponseDto;
import com.moa.backend.member.model.dto.MemberResponseDto;
import com.moa.backend.member.model.dto.ReplyResponseDto;
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
	
	public Member login(String email) {
		return mapper.findByEmail(email);
	}
//membersMe APi에서 nickname을 주기 때문에 중복 가능성이 있습니다 확인후 처리해주세요
//	public Member getMember(Long memberId) {
//	    return mapper.getMember(memberId);
//	}
	public MemberResponseDto membersMe(long memberId) {
		return mapper.memberDetail(memberId);
	}
	
	public List<BoardResponseDto> selectMyBoards(long memberId) {
		return mapper.selectMyBoards(memberId);
	}

	public List<ReplyResponseDto> selectMyReplies(long memberId) {
		return mapper.selectMyReplies(memberId);
	}
	
	
}
