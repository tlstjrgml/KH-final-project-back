package com.moa.backend.board.model.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Board {
	private Long boardId;
	private String boardTitle;
	private String boardContent;
	private String boardType;
	private String boardStatus;

	private LocalDateTime createDate;
	private LocalDateTime modifyDate;

	private Integer views;

	private Long memberId;
	private Long welfareId;
}
