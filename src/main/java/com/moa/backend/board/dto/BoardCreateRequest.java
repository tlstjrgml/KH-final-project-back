package com.moa.backend.board.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardCreateRequest {
	private String boardTitle;
	private String boardContent;
	private String boardType;

	private Long memberId;
	private Long welfareId;

	private List<MultipartFile> files;
}
