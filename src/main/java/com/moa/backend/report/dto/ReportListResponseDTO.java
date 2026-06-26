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
	
	// 조인(JOIN)을 통해 가져올 게시판 카테고리와 실제 내용
	private String targetCategory; // 예: "FRE", "REV" (댓글일 경우 부모 게시판의 카테고리)
	private String targetContent; // 예: 게시글 제목 또는 댓글 본문 내용
	private String targetStatus;
}
