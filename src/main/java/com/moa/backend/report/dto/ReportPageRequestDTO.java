package com.moa.backend.report.dto;

import com.moa.backend.common.util.page.PageRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ReportPageRequestDTO extends PageRequest {
	// 특정 글이나 댓글 번호로 검색하고 싶을 때 (선택)
	private Long targetId;

	// 게시글 종류별 필터링 ("FRE", "REV", "REP" / 선택)
	private String targetType;

	// 처리 상태별 필터링 ("PENDING", "DONE", "REJECT" / 선택)
	private String status;
}
