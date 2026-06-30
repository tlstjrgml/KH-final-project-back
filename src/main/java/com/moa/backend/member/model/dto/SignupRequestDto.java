package com.moa.backend.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {
	@NotBlank(message = "이메일을 입력해주세요.")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "올바른 이메일 형식이 아닙니다.")
	private String email;
	
	private String password;
	private String nickname;
	private String inputCode;
	private String expireTime;
	private String token;
	
}
