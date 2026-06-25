package com.moa.backend.board.dto;

import com.moa.backend.common.util.page.PageRequest;

import com.moa.backend.common.util.page.PageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true) // 부모 필드(page, limit)까지 toString에 출력
public class BoardPageRequest extends PageRequest {
	// 프론트엔드에서 파라미터로 넘겨줄 게시판 타입 ("FRE", "REV", "NOT")
	private String boardType;
	
    // 검색어
    private String keyword; 
    
    // 내용
    private String boardContent; 
    
    // 작성자 닉네임
    private String nickname;
    
    // 정렬 조건 ("latest", "oldest", "views")
    private String sort = "latest"; 
}
