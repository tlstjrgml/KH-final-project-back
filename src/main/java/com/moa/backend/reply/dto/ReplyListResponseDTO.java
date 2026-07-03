package com.moa.backend.reply.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReplyListResponseDTO {
	private Long replyId;
	private Long memberId;
	private String replyContent;
	private LocalDateTime createDate;
	private String replyStatus;
	private Long refId; 				 
	private String code;				// 'B': 원댓글, 'R': 대댓글
	private String writerNickname; 		 
}