package com.moa.backend.board.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardUpdateRequestDTO {
	private String boardTitle;
	private String boardContent;
	private Long welfareId;

	private List<Long> deleteFileIds;

	private List<MultipartFile> newFiles;
}
