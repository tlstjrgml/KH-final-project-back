package com.moa.backend.report.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportDetailResponseDTO<T> {
	// 1. 신고 기본 정보
	private Long reportId;
	private Long targetId;
	private String targetType; 			// "FRE", "REV", "REP"
	private LocalDateTime reportDate;
	private String reason;
	private String status;
	private String reportResult;

	// 2. 신고자 정보
	private Long memberId;
	private String writerNickname;
	private String profileImg;

	// 3. 제네릭을 활용한 동적 상세 정보
	private T targetDetails;
}