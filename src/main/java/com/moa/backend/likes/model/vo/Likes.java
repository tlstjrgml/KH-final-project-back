package com.moa.backend.likes.model.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Likes {
	private Long memberId;
	private Long boardId;
}
