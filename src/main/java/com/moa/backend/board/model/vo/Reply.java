package com.moa.backend.board.model.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Reply {
	
	private Long replyId;
	private Long memberId;
	private String replyContent;
	private LocalDateTime createDate;
	private LocalDateTime modifyDate;
	private String replyStatus;
	private Long refId;
	private String code;

}

