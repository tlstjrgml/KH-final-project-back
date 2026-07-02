package com.moa.backend.board.dto;

import com.moa.backend.common.util.page.PageRequest;

import com.moa.backend.common.util.page.PageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)  
public class BoardPageRequest extends PageRequest {
 
	private String boardType;
	
   
    private String keyword; 
    
     
    private String boardContent; 
    
     
    private String nickname;
    
 
    private String sort = "latest"; 
}
