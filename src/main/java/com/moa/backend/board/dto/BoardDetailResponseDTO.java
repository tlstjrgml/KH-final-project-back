package com.moa.backend.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.moa.backend.board.model.vo.Attachment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDetailResponseDTO {
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
	
    private String writerNickname; 	
    private Integer likeCount;		 
    
    private Boolean isLiked; 		 
    private Boolean isOwner;		 
    
    private List<Attachment> attachments;	 
}
