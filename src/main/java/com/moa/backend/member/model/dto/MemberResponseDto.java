package com.moa.backend.member.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberResponseDto{
	private String email;
	private String nickname;
	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date birthDate;
	private String gender;
	private String phone;
	private String region;
	private String jobStatus;
	private int incomeLevel;
	private String profileImg;
	private int boardCount;
	private int replyCount;
	private int wishCount;
}