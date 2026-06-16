package com.moa.backend.common.util.page;

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
public class PageRequest {
	private int page = 1; // 요청 페이지
	private int limit = 10; // 페이지당 출력 개수

	// 건너뛸 행의 갯수
	public int getOffset() {
		return (this.page - 1) * this.limit;
	}
}
