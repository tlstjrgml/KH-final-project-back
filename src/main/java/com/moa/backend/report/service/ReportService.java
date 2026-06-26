package com.moa.backend.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.backend.common.util.page.PageResponse;
import com.moa.backend.common.util.page.Pagination;
import com.moa.backend.notification.SseEmitterRepository;
import com.moa.backend.report.dto.ReportCreateRequestDTO;
import com.moa.backend.report.dto.ReportListResponseDTO;
import com.moa.backend.report.dto.ReportPageRequestDTO;
import com.moa.backend.report.dto.ReportUpdateRequestDTO;
import com.moa.backend.report.model.mapper.ReportMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
	
	private final ReportMapper reportMapper;
	private final SseEmitterRepository sseEmitterRepository;
	
	@Transactional(rollbackFor = Exception.class)
	public void insertReport(ReportCreateRequestDTO request) {
		String type = request.getTargetType();
		if (!"FRE".equals(type) && !"REV".equals(type) && !"REP".equals(type)) {
			throw new IllegalArgumentException("잘못된 대상 타입입니다. (FRE, REV, REP만 가능)");
		}
		
		reportMapper.insertReport(request);
		
	}

	public PageResponse<ReportListResponseDTO> selectReportList(ReportPageRequestDTO reportPageRequest) {
		int totalCount = reportMapper.selectReportCount(reportPageRequest);

		List<ReportListResponseDTO> reportList = reportMapper.selectReportList(reportPageRequest);

		Pagination pagination = new Pagination(reportPageRequest, totalCount);

		return new PageResponse<>(reportList, pagination);
	}

	@Transactional(rollbackFor = Exception.class)
    public boolean updateReport(ReportUpdateRequestDTO reportUpdateRequest) {
        int result = reportMapper.updateReport(reportUpdateRequest);
        if(result > 0) {
            Long memberId = reportMapper.selectReporterIdByReportId(reportUpdateRequest.getReportId());
            System.out.println("신고자 memberId: " + memberId); // 추가
            if("DONE".equals(reportUpdateRequest.getStatus())){
                sseEmitterRepository.sendNotification(memberId, "신고가 처리되었습니다. 사유: " + reportUpdateRequest.getReportResult());
            }else if("REJECT".equals(reportUpdateRequest.getStatus())) {
                sseEmitterRepository.sendNotification(memberId, "신고가 반려되었습니다. 사유: " + reportUpdateRequest.getReportResult());
            }
        }
        
        
        return result > 0;
    }
	
}
