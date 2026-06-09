package com.moa.backend.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.backend.member.model.vo.Member;
import com.moa.backend.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
	private final MemberService mService;
	private final BCryptPasswordEncoder bcrypt;

	// 테스트용 http://localhost:8080/member/test-insert
	@GetMapping("/test-insert")
	public String testInsert() {
		try {
			// 1. 가짜 데이터(더미) 담은 VO 객체 생성
			Member testMember = new Member();
			testMember.setEmail("test777@naver.com"); // 유니크 값 테스트시 변경할 것

			// 스프링 시큐리티 비번 암호화 적용해서
			String encPwd = bcrypt.encode("1234");
			testMember.setPassword(encPwd);

			testMember.setLoginType("LOCAL");
			testMember.setNickname("테스트노예");

			// 2. 서비스 호출하기
			int result = mService.insertMember(testMember);

			// 3. 결과 반환
			if (result > 0) {
				return "DB에 데이터 집어넣기 성공!";
			} else {
				return "삽입된 행이 없음";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "에러" + e.getMessage();
		}
	}

	// 이메일로 회원가입하기
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody Member member) {
		// 1. 비밀번호 암호화 하기
		member.setPassword(bcrypt.encode(member.getPassword()));

		// 2. 이메일로 가입이므로 로그인타입 LOCAL로
		member.setLoginType("LOCAL");

		// 3. DB에 넣기
		int result = mService.insertMember(member);

		if (result > 0) {
			// 200 OK
			return ResponseEntity.ok("회원가입이 성공");
		} else {
			// 400
			return ResponseEntity.badRequest().body("회원가입 실패");
		}
	}
	
	

}
