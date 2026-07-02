package com.moa.backend.report.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportUpdateRequestDTO {
	private Long reportId;        
    private String status;        
    private String reportResult;  
}
