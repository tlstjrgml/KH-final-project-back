package com.moa.backend.board.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Attachment {

	private Long attmId;
	private String originalName;
	private String renameName;
	private String attmPath;
	private String attmStatus;
	private Long boardId;
}
