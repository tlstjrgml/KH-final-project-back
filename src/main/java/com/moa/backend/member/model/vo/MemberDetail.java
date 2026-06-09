package com.moa.backend.member.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@ToString

public class MemberDetail {
	private Long memberId;
	private String name;
	private Date birthDate;
	private String gender;
	private String phone;
	private String region;
	private String jobStatus;
	private Integer incomeLevel;
	private String profileImg;
	
	
}
