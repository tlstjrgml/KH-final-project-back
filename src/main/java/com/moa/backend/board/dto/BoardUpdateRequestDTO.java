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

	// 삭제 기존 첨부파일의 ID 목록
	private List<Long> deleteFileIds;

	// 새로 추가한 파일 파일들
	private List<MultipartFile> newFiles;
}
