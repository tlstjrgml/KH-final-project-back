package com.moa.backend.report.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportedReplyInfo {
	private Long replyId;
	private Long memberId;
	private String replyContent;
	private LocalDateTime createDate;
	private String replyStatus;
	private Long refId; 				// 게시글 번호 또는 부모 댓글 번호
	private String code;				// 'B': 원댓글, 'R': 대댓글

	private String replyWriterNickname; 	// 작성자 닉네임
	private String replyWriterProfileImg;	// 작성자 프로필 이미지 	
}
