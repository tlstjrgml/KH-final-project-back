package com.moa.backend.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.member.model.vo.Member;

@Mapper
public interface MemberMapper {
	int insertMember(Member m);
}
