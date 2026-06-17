package com.moa.backend.member.model.dto;

import java.util.Date;
import lombok.Getter;

@Getter
public class MemberResponseDto{
	private String email;
	private String nickname;
	private String name;
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