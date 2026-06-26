package com.moa.backend.report.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.report.dto.ReportCreateRequestDTO;
import com.moa.backend.report.dto.ReportDetailResponseDTO;
import com.moa.backend.report.dto.ReportListResponseDTO;
import com.moa.backend.report.dto.ReportPageRequestDTO;
import com.moa.backend.report.dto.ReportUpdateRequestDTO;
import com.moa.backend.report.dto.ReportedBoardInfo;
import com.moa.backend.report.dto.ReportedReplyInfo;

@Mapper
public interface ReportMapper {

	int insertReport(ReportCreateRequestDTO dto);

	int selectReportCount(ReportPageRequestDTO reportPageRequest);

	List<ReportListResponseDTO> selectReportList(ReportPageRequestDTO reportPageRequest);
	
	int updateReport(ReportUpdateRequestDTO dto);

	ReportDetailResponseDTO<Object> selectReportDetailBase(Long reportId);

	ReportedReplyInfo selectReportedReply(Long targetId);
}