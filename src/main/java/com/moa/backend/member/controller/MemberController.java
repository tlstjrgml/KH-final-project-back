package com.moa.backend.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.backend.common.util.EmailAuthProvider;

import lombok.RequiredArgsConstructor;
import com.moa.backend.common.config.jwt.JwtProvider;
import com.moa.backend.member.model.vo.Member;
import com.moa.backend.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
	private final MemberService mService;
	private final BCryptPasswordEncoder bcrypt;
	private final EmailAuthProvider authProvider;
	private final JwtProvider jwtProvider; 
	
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
	public ResponseEntity<String> signup(@RequestBody Map<String, Object> requestData) {

		// 1. JSON에서 이메일 인증 데이터들 먼저 꺼내기
		String token = (String) requestData.get("token");
		String expireTimeStr = String.valueOf(requestData.get("expireTime"));
		String inputCode = (String) requestData.get("inputCode"); // 유저가 리액트 화면에 입력한 6자리 번호
		String email = (String) requestData.get("email"); // 가입할 이메일

		// 2. 데이터 누락 및 만료 시간 검사
		if (token == null || expireTimeStr == null || inputCode == null || email == null) {
			return ResponseEntity.badRequest().body("회원가입 요청 데이터가 올바르지 않습니다.");
		}

		long expireTime = Long.parseLong(expireTimeStr);
		if (System.currentTimeMillis() > expireTime) {
			return ResponseEntity.badRequest().body("이메일 인증 시간이 만료되었습니다. 인증을 다시 해주세요.");
		}

		// 3. 위조 및 번호 일치 여부 최종 검증
		boolean isVerified = authProvider.verifyToken(email, inputCode, expireTime, token);
		if (!isVerified) {
			return ResponseEntity.badRequest().body("잘못되거나 위조된 이메일 인증 토큰입니다.");
		}

		// 4. 이메일, 닉네임 설정
		Member member = new Member();
		member.setEmail(email);
		member.setNickname((String) requestData.get("nickname"));

		// 5. 비밀번호 암호화
		String rawPassword = (String) requestData.get("password");
		member.setPassword(bcrypt.encode(rawPassword));
		member.setLoginType("LOCAL"); // 이메일 가입이므로 LOCAL

		// 6. DB에 저장
		int result = mService.insertMember(member);

		if (result > 0) {
			return ResponseEntity.ok("회원가입 성공");
		} else {
			return ResponseEntity.badRequest().body("회원가입 실패");
		}
	}
	
	

	// 인증 이메일 요청
	@PostMapping("/echeck")
	public ResponseEntity<Map<String, Object>> checkEmail(@RequestBody Map<String, String> request) {
		String email = request.get("email");

		// 이메일 값이 비어있다면 400 에러 
		if (email == null || email.isEmpty()) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", "이메일을 입력해주세요.");
			return ResponseEntity.badRequest().body(errorResponse);
		}
		
		
		// 6자리 인증번호 생성
		String verificationCode = authProvider.generateVerificationCode();
		
		// 이메일 테스트
		System.out.println("=========================================");
		System.out.println("[이메일 인증 발송 시뮬레이션]");
		System.out.println("수신 이메일 : " + email);
		System.out.println("생성된 인증코드 : " + verificationCode);
		System.out.println("=========================================");
		
		authProvider.sendAuthEmail(email, verificationCode);

		// 유효기간 설정 (현재 시간 + 5)
		long expireTime = System.currentTimeMillis() + 5 * 60 * 1000;

		// 이메일, 인증코드, 유효기간을 섞어서 HMAC 토큰 발행
		String hmacToken = authProvider.createHmacToken(email, verificationCode, expireTime);

		// 리액트에게 돌려줄 바구니(Map)에 데이터 담기
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("email", email);
			responseBody.put("expireTime", expireTime); // 리액트가 화면에 카운트다운(5:00) 그릴 때 쓸 원문 시간
		responseBody.put("token", hmacToken);       // 리액트가 보관하다가 나중에 인증할 때 다시 들고 올 봉인장 해시값
		
		// ⭐ [테스트용 꿀팁] 지금은 진짜 메일이 안 가니까, 포스트맨 응답창에서 
		// 인증코드가 뭔지 바로 볼 수 있게 슬쩍 끼워 넣어 줍니다. (나중에 진짜 메일 보낼 땐 이 줄만 지우면 됩니다!)
		responseBody.put("DEBUG_ONLY_CODE", verificationCode); 

		// 리액트에게 200 OK 사인이랑 같이 데이터 던지기!
		return ResponseEntity.ok(responseBody);
	}
	
	// 이메일 인증코드 검증
	@PostMapping("/everify")
	public ResponseEntity<Map<String, Object>> verifyEmail(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String expireTimeStr = request.get("expireTime");
		String inputCode = request.get("inputCode"); // 유저가 가입 화면에 입력한 6자리 번호
		String token = request.get("token"); // 리액트가 보관하고 있던 원래의 HMAC 토큰
		
		Map<String, Object> responseBody = new HashMap<>();
		
		// 1. 필수 데이터 누락 체크
		if (email == null || expireTimeStr == null || inputCode == null || token == null) {
			responseBody.put("success", false);
			responseBody.put("message", "잘못된 인증 요청입니다. 데이터가 누락되었습니다.");
			return ResponseEntity.badRequest().body(responseBody);
		}

		long expireTime = Long.parseLong(expireTimeStr);

		// 2. 유효시간 만료 여부 체크
		if (System.currentTimeMillis() > expireTime) {
			responseBody.put("success", false);
			responseBody.put("message", "인증 시간이 만료되었습니다. 인증번호를 다시 받아주세요.");
			return ResponseEntity.badRequest().body(responseBody);
		}

		// 3. 유틸리티를 호출해서 번호 일치 여부 체크
		boolean isVerified = authProvider.verifyToken(email, inputCode, expireTime, token);
		if (isVerified) {
			responseBody.put("success", true);
			responseBody.put("message", "이메일 인증이 성공적으로 완료되었습니다.");
			return ResponseEntity.ok(responseBody);
		} else {
			responseBody.put("success", false);
			responseBody.put("message", "인증번호가 일치하지 않거나 올바르지 않은 접근입니다.");
			return ResponseEntity.badRequest().body(responseBody);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String>request){
		String email = request.get("email");
		Member member = mService.login(email);
		
		if (member == null) {
		    return ResponseEntity.badRequest().body("존재하지 않는 이메일입니다.");
		}
		if(bcrypt.matches(request.get("password"), member.getPassword())) {
			
			String token = jwtProvider.generateToken(member.getMemberId());
			return ResponseEntity.ok(token);
		}else {
			return ResponseEntity.badRequest().body("아이디 또는 비밀번호가 일치하지 않습니다. 다시 시도해주세요");
		}
	}
}






























