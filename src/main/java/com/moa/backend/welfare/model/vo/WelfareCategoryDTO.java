package com.moa.backend.welfare.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class WelfareCategoryDTO {
	private String mainId;
	private String subId;
	private String subName;
}
