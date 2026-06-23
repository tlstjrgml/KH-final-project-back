package com.moa.backend.member.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberPasswordUpdateRequestDto {
	private String newPassword;
	private String confirmPassword;
}
