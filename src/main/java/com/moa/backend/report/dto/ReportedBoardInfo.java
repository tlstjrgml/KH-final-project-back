package com.moa.backend.report.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.moa.backend.board.model.vo.Attachment;

public class ReportedBoardInfo {
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
	
	private String boardWriterNickname; 	// 작성자 닉네임
	private String boardWriterProfileImg;	// 작성자 프로필 이미지 
    private Integer likeCount;		// 좋아요 수
    
    private List<Attachment> attachments;	// 첨부파일 목록

}
