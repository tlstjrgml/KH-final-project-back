package com.moa.backend.board.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDetailResponseDTO {
	// 기존 Board vo클래스와 동일한 부분
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
	
	// DTO 추가
    private String writerNickname; 	// 작성자 닉네임
    private Integer likeCount;		// 좋아요 수
    
    private Boolean isLiked; 		// 내가 좋아요 눌렀는지 여부
    private Boolean isOwner;			// 작성자 여부
}
