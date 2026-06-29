package com.moa.backend.wish.model.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString

public class WishResponseDto {
	private Long welfareId;
	private String lclsfNm;
	private String plcyNm;
	private String wishDate;
}
