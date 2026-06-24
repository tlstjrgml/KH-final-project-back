package com.moa.backend.report.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class ReportCreateRequestDTO {
    private Long targetId;      // 대상 번호
    private String targetType;  // 대상 타입 (FRE / REV / REP)
    private String reason;      // 신고 이유
    
    // 서비스단에서 로그인한 유저 ID를 심어줄 필드
    private Long memberId;      // 신고자 아이디
}