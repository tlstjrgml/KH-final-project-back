package com.moa.backend.member.model.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ReplyResponseDto {
	private int replyId;
	private String replyContent;
	private Date createDate;
	private String code;
	private String boardType;
	private int boardId;
}
