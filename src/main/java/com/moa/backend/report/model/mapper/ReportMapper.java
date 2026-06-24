package com.moa.backend.report.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.report.dto.ReportCreateRequestDTO;

@Mapper
public interface ReportMapper {

	int insertReport(ReportCreateRequestDTO dto);
}