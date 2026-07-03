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
	private int page = 1;  
	private int limit = 10;  

	public int getOffset() {
		return (this.page - 1) * this.limit;
	}
}
