package com.moa.backend.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.backend.common.util.page.PageResponse;
import com.moa.backend.common.util.page.Pagination;
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
        return result > 0;
    }
}
