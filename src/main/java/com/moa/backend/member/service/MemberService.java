package com.moa.backend.member.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.moa.backend.board.dto.BoardResponseDto;
import com.moa.backend.member.model.dto.MemberPasswordUpdateRequestDto;
import com.moa.backend.member.model.dto.MemberResponseDto;
import com.moa.backend.member.model.dto.MemberUpdateRequestDto;
import com.moa.backend.member.model.dto.ReplyResponseDto;
import com.moa.backend.member.model.mapper.MemberMapper;
import com.moa.backend.member.model.vo.Member;
import com.moa.backend.member.model.vo.MemberDetail;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberMapper mapper;
	private final BCryptPasswordEncoder bcrypt;
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

  // 1. 총 가입자 수 반환
	public int getTotalMemberCount() {
		return mapper.getTotalMemberCount();
	}

	// 2. 가입자 7일 추이 반환
	public List<Map<String, Object>> getSignupTrend() {
		return mapper.getSignupTrend();
	}

	// 3. 인기 복지 TOP 10 반환
	public List<Map<String, Object>> getTopWelfare() {
		return mapper.getTopWelfare();
	}
	
	public List<BoardResponseDto> selectMyBoards(long memberId) {
		return mapper.selectMyBoards(memberId);
	}

	public List<ReplyResponseDto> selectMyReplies(long memberId) {
		return mapper.selectMyReplies(memberId);
	}

	
	@Transactional
	public void updateMember(Long memberId, MemberUpdateRequestDto dto) {
		mapper.updateMember(memberId, dto);
		mapper.updateMemberDetail(memberId, dto);
	}
	
	public boolean updatePassword(Long memberId, MemberPasswordUpdateRequestDto dto) {
		if(!dto.getNewPassword().equals(dto.getConfirmPassword())){
			return false;
		}
		String encodedPassword = bcrypt.encode(dto.getNewPassword());
		mapper.updatePassword(memberId, encodedPassword);
		return true;
		
	}
	

}

