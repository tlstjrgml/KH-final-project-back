package com.moa.backend.report.dto;

import com.moa.backend.common.util.page.PageRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ReportPageRequestDTO extends PageRequest {
	private Long targetId;

	private String targetType;

	private String status;
}
