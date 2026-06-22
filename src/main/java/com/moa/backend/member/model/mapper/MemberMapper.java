package com.moa.backend.member.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.moa.backend.board.dto.BoardResponseDto;
import com.moa.backend.member.model.dto.MemberResponseDto;
import com.moa.backend.member.model.dto.ReplyResponseDto;
import com.moa.backend.member.model.vo.Member;
import com.moa.backend.member.model.vo.MemberDetail;

@Mapper
public interface MemberMapper {
	int insertMember(Member m);

	Member findByKakaoId(Long kakaoId);
	
	int insertMemberDetail(MemberDetail md);
	
	Member findByMemberId(Long memberId);
	
	Member findByEmail(String email);

	MemberResponseDto memberDetail(@Param("memberId") long memberId);

	List<BoardResponseDto> selectMyBoards(@Param("memberId") Long memberId);

	List<ReplyResponseDto> selectMyReplies(@Param("memberId") Long memberId);
}
