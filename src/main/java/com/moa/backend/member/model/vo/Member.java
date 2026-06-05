package com.moa.backend.member.model.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {
	private Long memberId;
    private String email;
    private String password;
    private String kakaoId;
    private String loginType;
    private String memberStatus;
    private String isAdmin;
    private LocalDateTime deletedDate;
    private LocalDateTime signupDate;
    private String nickname;
}
