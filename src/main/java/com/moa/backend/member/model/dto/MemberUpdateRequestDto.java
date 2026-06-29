package com.moa.backend.member.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberUpdateRequestDto {
	private String email;
	private String nickname;
	private String name;
	private String birthDate;
	private String gender;
	private String phone;
	private String region;
	private String jobStatus;
	private Integer incomeLevel;
}
