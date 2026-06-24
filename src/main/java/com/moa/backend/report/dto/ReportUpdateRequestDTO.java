package com.moa.backend.report.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportUpdateRequestDTO {
	private Long reportId;       // 어떤 신고를 수정할지 (PK)
    private String status;       // 변경할 상태 ("DONE", "REJECT")
    private String reportResult; // 관리자가 작성한 처리 결과 및 사유
}
