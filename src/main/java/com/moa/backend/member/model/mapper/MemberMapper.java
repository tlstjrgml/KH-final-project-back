package com.moa.backend.member.model.mapper;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.moa.backend.board.dto.BoardResponseDto;
import com.moa.backend.member.model.dto.MemberPasswordUpdateRequestDto;
import com.moa.backend.member.model.dto.MemberResponseDto;
import com.moa.backend.member.model.dto.MemberUpdateRequestDto;
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
	
	// 대시보드 통계용
	int getTotalMemberCount();
		
	List<Map<String, Object>> getSignupTrend();
	
	List<Map<String, Object>> getTopWelfare();

	List<BoardResponseDto> selectMyBoards(@Param("memberId") Long memberId);

	List<ReplyResponseDto> selectMyReplies(@Param("memberId") Long memberId);
	
	int updateMember(@Param("memberId") Long memberId, @Param("dto")MemberUpdateRequestDto dto);
	
	int updateMemberDetail(@Param("memberId") Long memberId, @Param("dto")MemberUpdateRequestDto dto);
	
	boolean updatePassword(@Param("memberId") Long memberId, @Param("encodedPassword")String encodedPassword);
	
	//s3파일 업로드
	void updateProfileImg(@Param("memberId") Long memberId, @Param("profileImgUrl") String profileImgUrl);

	int getAdminTotalMemberCount(Map<String, Object> paramMap);

	List<Member> getAdminMemberList(Map<String, Object> paramMap);

	void withdrawMember(Long memberId);

	void restoreMember(Long memberId);
}
