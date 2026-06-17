package com.moa.backend.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.moa.backend.member.model.dto.MemberResponseDto;
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

}
