package com.moa.backend.member.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moa.backend.board.dto.BoardResponseDto;
import com.moa.backend.common.config.jwt.CustomUserDetails;
import com.moa.backend.common.config.jwt.JwtProvider;
import com.moa.backend.common.util.EmailAuthProvider;
import com.moa.backend.common.util.page.PageRequest;
import com.moa.backend.common.util.page.PageResponse;
import com.moa.backend.common.util.page.Pagination;
import com.moa.backend.member.model.dto.MemberPasswordUpdateRequestDto;
import com.moa.backend.member.model.dto.MemberResponseDto;
import com.moa.backend.member.model.dto.MemberUpdateRequestDto;
import com.moa.backend.member.model.dto.ReplyResponseDto;
import com.moa.backend.member.model.dto.SignupRequestDto;
import com.moa.backend.member.model.vo.Member;
import com.moa.backend.member.model.vo.MemberDetail;
import com.moa.backend.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
	private final MemberService mService;
	private final BCryptPasswordEncoder bcrypt;
	private final EmailAuthProvider authProvider;
	private final JwtProvider jwtProvider;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto dto) {

		String token = dto.getToken();
		String expireTimeStr = dto.getExpireTime();
		String inputCode = dto.getInputCode();
		String email = dto.getEmail();

		if (token == null || expireTimeStr == null || inputCode == null || email == null) {
			return ResponseEntity.badRequest().body("회원가입 요청 데이터가 올바르지 않습니다.");
		}

		long expireTime = Long.parseLong(expireTimeStr);
		if (System.currentTimeMillis() > expireTime) {
			return ResponseEntity.badRequest().body("이메일 인증 시간이 만료되었습니다. 인증을 다시 해주세요.");
		}

		boolean isVerified = authProvider.verifyToken(email, inputCode, expireTime, token);
		if (!isVerified) {
			return ResponseEntity.badRequest().body("잘못되거나 위조된 이메일 인증 토큰입니다.");
		}

		Member member = new Member();
		member.setEmail(email);
		member.setNickname(dto.getNickname());
		String rawPassword = dto.getPassword();
		member.setPassword(bcrypt.encode(rawPassword));
		member.setLoginType("LOCAL");

		int result = mService.insertMember(member);

		if (result > 0) {
			MemberDetail memberDetail = new MemberDetail();
			memberDetail.setMemberId(member.getMemberId());
			mService.insertMemberDetail(memberDetail);
			return ResponseEntity.ok("회원가입 성공");
		} else {
			return ResponseEntity.badRequest().body("회원가입 실패");
		}
	}

	@GetMapping("/check-email")
	public ResponseEntity<?> checkEmailDuplicate(@RequestParam("email") String email){
		Member member = mService.findByEmail(email);
		if(member != null) {
			return ResponseEntity.badRequest().body("이미 사용중인 아이디(이메일)입니다.");
		}
		return ResponseEntity.ok("사용가능한 아이디(이메일)입니다.");
	}

	@PostMapping("/echeck")
	public ResponseEntity<Map<String, Object>> checkEmail(@RequestBody Map<String, String> request) {
		String email = request.get("email");

		if (email == null || email.isEmpty()) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", "이메일을 입력해주세요.");
			return ResponseEntity.badRequest().body(errorResponse);
		}

		String verificationCode = authProvider.generateVerificationCode();

		authProvider.sendAuthEmail(email, verificationCode);

		long expireTime = System.currentTimeMillis() + 5 * 60 * 1000;

		String hmacToken = authProvider.createHmacToken(email, verificationCode, expireTime);

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("email", email);
		responseBody.put("expireTime", expireTime);
		responseBody.put("token", hmacToken);

		return ResponseEntity.ok(responseBody);
	}

	@PostMapping("/everify")
	public ResponseEntity<Map<String, Object>> verifyEmail(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String expireTimeStr = request.get("expireTime");
		String inputCode = request.get("inputCode");
		String token = request.get("token");

		Map<String, Object> responseBody = new HashMap<>();

		if (email == null || expireTimeStr == null || inputCode == null || token == null) {
			responseBody.put("success", false);
			responseBody.put("message", "잘못된 인증 요청입니다. 데이터가 누락되었습니다.");
			return ResponseEntity.badRequest().body(responseBody);
		}

		long expireTime = Long.parseLong(expireTimeStr);

		if (System.currentTimeMillis() > expireTime) {
			responseBody.put("success", false);
			responseBody.put("message", "인증 시간이 만료되었습니다. 인증번호를 다시 받아주세요.");
			return ResponseEntity.badRequest().body(responseBody);
		}

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

		if ("BANNED".equals(member.getMemberStatus())) {
			return ResponseEntity.status(403).body("강제 탈퇴된 회원입니다.");
		}

		if ("DELETE".equals(member.getMemberStatus())) {
			return ResponseEntity.status(403).body("탈퇴한 회원입니다.");
		}

		if(bcrypt.matches(request.get("password"), member.getPassword())) {
			String token = jwtProvider.generateToken(member.getMemberId(), member.getIsAdmin(), member.getNickname());
			return ResponseEntity.ok(token);
		}else {
			return ResponseEntity.badRequest().body("아이디 또는 비밀번호가 일치하지 않습니다. 다시 시도해주세요");
		}
	}

	@GetMapping("/me")
	public ResponseEntity<MemberResponseDto> membersMe(@AuthenticationPrincipal CustomUserDetails userDetails) {
	    long memberId = userDetails.getMemberId();
	    MemberResponseDto dto = mService.membersMe(memberId);
	    return ResponseEntity.ok(dto);
	}

	@GetMapping("/admin/dashboard/total-members")
	public ResponseEntity<Integer> getTotalMemberCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null || !"Y".equals(userDetails.getIsAdmin())) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(mService.getTotalMemberCount());
	}

	@GetMapping("/admin/dashboard/signup-trend")
	public ResponseEntity<List<Map<String, Object>>> getSignupTrend(
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null || !"Y".equals(userDetails.getIsAdmin())) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(mService.getSignupTrend());
	}

	@GetMapping("/admin/dashboard/top-welfare")
	public ResponseEntity<List<Map<String, Object>>> getTopWelfare(
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null || !"Y".equals(userDetails.getIsAdmin())) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(mService.getTopWelfare());
	}

	@GetMapping("/me/boards")
	public ResponseEntity<List<BoardResponseDto>> membersMeBoards(@AuthenticationPrincipal CustomUserDetails userDetails){
		long memberId = userDetails.getMemberId();
		List<BoardResponseDto> dtoList = mService.selectMyBoards(memberId);
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/me/replies")
	public ResponseEntity<List<ReplyResponseDto>> membersMeReplies(@AuthenticationPrincipal CustomUserDetails userDetails){
		long memberId = userDetails.getMemberId();
		List<ReplyResponseDto> dtoList = mService.selectMyReplies(memberId);
		return ResponseEntity.ok(dtoList);
	}

	@PatchMapping("/me")
	public ResponseEntity<?> updateMember(
			@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestBody MemberUpdateRequestDto dto){
		long memberId = userDetails.getMemberId();
		mService.updateMember(memberId, dto);
		return ResponseEntity.ok("수정 완료");
	}

	@PatchMapping("/me/password")
	public ResponseEntity<?> updatePassword(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MemberPasswordUpdateRequestDto dto){
		long memberId = userDetails.getMemberId();
		String result = mService.updatePassword(memberId, dto);
		if(result != null) {
			return ResponseEntity.badRequest().body(result);
		}
		return ResponseEntity.ok("비밀번호 변경 완료");
	}

	@PatchMapping("/me/profile-image")
	public ResponseEntity<?> updateProfileImage(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestParam("file")MultipartFile file){
		long memberId = userDetails.getMemberId();
		try {
		    String url = mService.updateProfileImg(memberId, file);
		    return ResponseEntity.ok(url);
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("프로필 이미지 업로드 실패: " + e.getMessage());
		}
	}

    @DeleteMapping("/me")
    public ResponseEntity<?> withdrawMyAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody(required = false) Map<String, String> request) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요한 서비스입니다.");
        }

        long memberId = userDetails.getMemberId();

        Member member = mService.findByMemberId(memberId);
        if (member == null) {
            return ResponseEntity.status(404).body("존재하지 않는 회원 정보입니다.");
        }

        if (request == null || !request.containsKey("password")) {
            return ResponseEntity.badRequest().body("비밀번호 확인이 필요합니다.");
        }

        String rawPassword = request.get("password");
        if (!bcrypt.matches(rawPassword, member.getPassword())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않아 탈퇴할 수 없습니다.");
        }

        try {
            mService.withdrawMember(memberId);
            return ResponseEntity.ok("회원 탈퇴가 정상적으로 처리되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("회원 탈퇴 처리 중 서버 오류가 발생했습니다.");
        }
    }

	@GetMapping("/admin/list")
	public ResponseEntity<?> getAdminMemberList(@AuthenticationPrincipal CustomUserDetails userDetails,
	        PageRequest pageRequest,
	        @RequestParam(value = "keyword", required = false) String keyword,
	        @RequestParam(value = "sort", defaultValue = "latest") String sort,
	        @RequestParam(value = "status", defaultValue = "ACTIVE") String status) {

	    if (userDetails == null || !"Y".equals(userDetails.getIsAdmin())) {
	        return ResponseEntity.status(403).body("관리자 권한이 없습니다.");
	    }

	    try {
	        Map<String, Object> paramMap = new HashMap<>();
	        paramMap.put("sort", sort);
	        paramMap.put("keyword", keyword);
	        paramMap.put("status", status);

	        paramMap.put("limit", pageRequest.getLimit());
	        paramMap.put("offset", pageRequest.getOffset());

	        int totalElements = mService.getAdminTotalMemberCount(paramMap);
	        Pagination pagination = new Pagination(pageRequest, totalElements);

	        List<Member> content = mService.getAdminMemberList(paramMap);

	        PageResponse<Member> response = new PageResponse<>(content, pagination);

	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("회원 목록 조회 중 서버 오류가 발생했습니다.");
	    }
	}

	@PatchMapping("/admin/{memberId}/withdraw")
	public ResponseEntity<?> withdrawMember(@AuthenticationPrincipal CustomUserDetails userDetails,
			@PathVariable("memberId") Long memberId) {

		if (userDetails == null || !"Y".equals(userDetails.getIsAdmin())) {
			return ResponseEntity.status(403).body("관리자 권한이 없습니다.");
		}

		try {
			mService.kickMember(memberId);
			return ResponseEntity.ok("성공적으로 탈퇴 처리되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("탈퇴 처리 중 서버 오류가 발생했습니다.");
		}
	}

	@PatchMapping("/admin/{memberId}/restore")
	public ResponseEntity<?> restoreMember(@AuthenticationPrincipal CustomUserDetails userDetails,
	        @PathVariable("memberId") Long memberId) {

	    if (userDetails == null || !"Y".equals(userDetails.getIsAdmin())) {
	        return ResponseEntity.status(403).body("관리자 권한이 없습니다.");
	    }

	    try {
	        mService.restoreMember(memberId);
	        return ResponseEntity.ok("성공적으로 복구 처리되었습니다.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("복구 처리 중 서버 오류가 발생했습니다.");
	    }
	}

	@DeleteMapping("/me/profile-image")
	public ResponseEntity<?> deleteProfileImg(@AuthenticationPrincipal CustomUserDetails userDetail){
		long memberId = userDetail.getMemberId();
		try {
			mService.deleteProfileImg(memberId);
			return ResponseEntity.ok("프로필 이미지 삭제 완료");
		}catch(Exception e){
			return ResponseEntity.badRequest().body("프로필 이미지 삭제 실패: " + e.getMessage());
		}
	}

	@PostMapping("/find-pw/everify")
	public ResponseEntity<Map<String, Object>> findPwEverify(@RequestBody Map<String, String> request) {
	    String email = request.get("email");
	    String expireTimeStr = request.get("expireTime");
	    String inputCode = request.get("inputCode");
	    String token = request.get("token");

	    Map<String, Object> responseBody = new HashMap<>();

	    if (email == null || expireTimeStr == null || inputCode == null || token == null) {
	        responseBody.put("success", false);
	        responseBody.put("message", "잘못된 인증 요청입니다. 데이터가 누락되었습니다.");
	        return ResponseEntity.badRequest().body(responseBody);
	    }

	    long expireTime = Long.parseLong(expireTimeStr);

	    if (System.currentTimeMillis() > expireTime) {
	        responseBody.put("success", false);
	        responseBody.put("message", "인증 시간이 만료되었습니다. 인증번호를 다시 받아주세요.");
	        return ResponseEntity.badRequest().body(responseBody);
	    }

	    boolean isVerified = authProvider.verifyToken(email, inputCode, expireTime, token);
	    if (!isVerified) {
	        responseBody.put("success", false);
	        responseBody.put("message", "인증번호가 일치하지 않거나 올바르지 않은 접근입니다.");
	        return ResponseEntity.badRequest().body(responseBody);
	    }

	    String tempPassword = authProvider.generateTempPassword();

	    String encodedPassword = bcrypt.encode(tempPassword);
	    mService.updateTempPassword(email, encodedPassword);

	    authProvider.sendTempPasswordEmail(email, tempPassword);

	    responseBody.put("success", true);
	    responseBody.put("message", "임시 비밀번호가 이메일로 발송되었습니다.");
	    return ResponseEntity.ok(responseBody);
	}

	@PostMapping("/find-pw/echeck")
	public ResponseEntity<Map<String, Object>> findPwEcheck(@RequestBody Map<String, String> request) {
	    String email = request.get("email");

	    if (email == null || email.isEmpty()) {
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("success", false);
	        errorResponse.put("message", "이메일을 입력해주세요.");
	        return ResponseEntity.badRequest().body(errorResponse);
	    }

	    if (mService.findByEmail(email) == null) {
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("success", false);
	        errorResponse.put("message", "존재하지 않는 이메일입니다.");
	        return ResponseEntity.badRequest().body(errorResponse);
	    }

	    String verificationCode = authProvider.generateVerificationCode();
	    authProvider.sendAuthEmail(email, verificationCode);

	    long expireTime = System.currentTimeMillis() + 5 * 60 * 1000;
	    String hmacToken = authProvider.createHmacToken(email, verificationCode, expireTime);

	    Map<String, Object> responseBody = new HashMap<>();
	    responseBody.put("email", email);
	    responseBody.put("expireTime", expireTime);
	    responseBody.put("token", hmacToken);

	    return ResponseEntity.ok(responseBody);
	}
}