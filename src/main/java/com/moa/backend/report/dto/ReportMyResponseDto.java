package com.moa.backend.report.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor

public class ReportMyResponseDto {
	private Long reportId;
	private String targetType;
	private Date reportDate;
	private String status;
	private String reportResult;
}
