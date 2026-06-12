package com.moa.backend.welfare.batch;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/welfare")
@RequiredArgsConstructor

public class AdminWelfareController{
	private final WelfareBatchService welfareBatchService;
	
	@PostMapping("/refresh")
	public ResponseEntity<?> manualRefresh(){
		try {
			welfareBatchService.fetchAndSaveAll(1);
			return ResponseEntity.ok("갱신 완료!");
		}catch (Exception e) {
			return ResponseEntity.internalServerError().body("갱신 실패: " + e.getMessage());
		}
	}
}