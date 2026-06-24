package com.moa.backend.report.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.backend.common.config.jwt.CustomUserDetails;
import com.moa.backend.report.dto.ReportCreateRequestDTO;
import com.moa.backend.report.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
	
	private final ReportService reportService;
	
	@PostMapping
    public ResponseEntity<?> createReport(
            @RequestBody ReportCreateRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요한 서비스입니다.");
            }
            
            // 토큰에서 추출한 로그인 유저 ID를 DTO에 세팅
            request.setMemberId(userDetails.getMemberId());
            
            // 서비스 호출
            reportService.insertReport(request);
            
            return ResponseEntity.status(HttpStatus.CREATED).body("신고가 정상적으로 접수되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("신고 접수 중 오류가 발생했습니다.");
        }
    }

	
	
}
