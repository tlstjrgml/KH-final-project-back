package com.moa.backend.report.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class ReportCreateRequestDTO {
    private Long targetId;       
    private String targetType;  
    private String reason;      
    
    private Long memberId;      
}