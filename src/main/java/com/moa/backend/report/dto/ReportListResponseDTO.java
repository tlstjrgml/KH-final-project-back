package com.moa.backend.report.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportListResponseDTO {
	private Long reportId;
	private Long targetId;
	
	private String targetType;	// FRE, REV, REP
	
	private LocalDateTime reportDate;
	private String reason; 		// NULL 허용 
	
	private String status;		//PENDING, DONE, REJECT / DEFAULT: PENDING)
	private String reportResult;
	private Long memberId;
	
	private String writerNickname; // JOIN으로 긁어올 작성자 닉네임
	private String profileImg;

}
