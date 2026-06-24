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
	
	private String targetType;	// FRE, REV, REP
	
	private LocalDateTime reportDate;
	private String reason; 		// NULL 허용 
	
	private String ;		//PENDING, DONE, REJECT / DEFAULT: PENDING)
	private String reportResult;
	private Long memberId;
}
