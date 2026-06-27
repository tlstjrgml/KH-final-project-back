package com.moa.backend.report.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.backend.common.config.jwt.CustomUserDetails;
import com.moa.backend.common.util.page.PageResponse;
import com.moa.backend.report.dto.ReportCreateRequestDTO;
import com.moa.backend.report.dto.ReportDetailResponseDTO;
import com.moa.backend.report.dto.ReportListResponseDTO;
import com.moa.backend.report.dto.ReportMyResponseDto;
import com.moa.backend.report.dto.ReportPageRequestDTO;
import com.moa.backend.report.dto.ReportUpdateRequestDTO;
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

	@PreAuthorize("hasRole('ADMIN')") 
    @GetMapping("/list")
    public ResponseEntity<?> getReportList(
    		@ModelAttribute ReportPageRequestDTO reportPageRequest) {
		try {
			PageResponse<ReportListResponseDTO> response = reportService.selectReportList(reportPageRequest);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("신고 목록을 불러오는 중 오류가 발생했습니다.");
		}
    }
	
	@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateReport(@RequestBody ReportUpdateRequestDTO reportUpdateRequest) {
        try {
            boolean isUpdated = reportService.updateReport(reportUpdateRequest);
            
            if (isUpdated) {
                return ResponseEntity.ok("신고 처리가 성공적으로 완료되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 신고 번호이거나 수정에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("신고 처리 중 서버 오류가 발생했습니다.");
        }
    }
	
	@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReportDetail(@PathVariable("reportId") Long reportId) {
        try {
            ReportDetailResponseDTO<?> response = reportService.getReportDetail(reportId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("신고 상세 내용을 불러오는 중 오류가 발생했습니다.");
        }
    }
	
	@GetMapping("/my")
	public ResponseEntity<?> getMyReportList(@AuthenticationPrincipal CustomUserDetails userDetails){
		
		if(userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		}
		long memberId = userDetails.getMemberId();
		List<ReportMyResponseDto> result = reportService.selectMyReportResponse(memberId);
		return ResponseEntity.ok(result);
	}
	
}
