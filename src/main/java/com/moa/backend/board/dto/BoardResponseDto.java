package com.moa.backend.board.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class BoardResponseDto {
	private int boardId;
	private String boardType;
	private String boardTitle;
	private int views;
	private Date createDate;
	private int likes;
}