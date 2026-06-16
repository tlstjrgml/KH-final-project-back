package com.moa.backend.board.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardListResponseDTO {
	private Long boardId;
	private String boardTitle;
	private String boardType;
	private Integer views;
	private LocalDateTime createDate;
	private String writerNickname; // JOIN으로 긁어올 작성자 닉네임
	private Integer likeCount; // JOIN으로 긁어올 좋아요 수
}
