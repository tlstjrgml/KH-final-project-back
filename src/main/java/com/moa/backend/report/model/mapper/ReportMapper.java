package com.moa.backend.report.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.report.dto.ReportCreateRequestDTO;
import com.moa.backend.report.dto.ReportListResponseDTO;
import com.moa.backend.report.dto.ReportPageRequestDTO;

@Mapper
public interface ReportMapper {

	int insertReport(ReportCreateRequestDTO dto);

	int selectReportCount(ReportPageRequestDTO reportPageRequest);

	List<ReportListResponseDTO> selectReportList(ReportPageRequestDTO reportPageRequest);
}