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
	
	private String targetType;	
	
	private LocalDateTime reportDate;
	private String reason; 		 
	
	private String status;		 
	private String reportResult;
	private Long memberId;
	
	private String writerNickname;  
	private String profileImg;
	
	private String targetCategory;  
	private String targetContent;  
	private String targetStatus;
}
