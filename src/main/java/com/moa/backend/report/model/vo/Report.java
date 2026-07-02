package com.moa.backend.report.model.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Report {
	private Long reportId;
	private Long targetId;
	
	private String targetType;	 
	
	private LocalDateTime reportDate;
	private String reason; 		 
	
	private String status;		 
	private String reportResult;
	private Long memberId;
}
