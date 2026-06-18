package com.moa.backend.welfare.model.mapper;

import java.util.List;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.welfare.model.vo.Welfare;
import com.moa.backend.welfare.model.vo.WelfareDetailDTO;
import com.moa.backend.welfare.model.vo.WelfareListDTO;
import com.moa.backend.welfare.model.vo.WelfareSearchDTO;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WelfareMapper {
 
	void mergeWelfare (Welfare vo);

	List<WelfareListDTO> getMainWelfare();

	List<WelfareListDTO> getTopten();

	List<WelfareListDTO> getWelfareList(WelfareSearchDTO params);

	int getWelfareCount(WelfareSearchDTO params);

	WelfareDetailDTO getWelfareDetail(Long id);

	List<WelfareListDTO> getRelatedWelfare(@Param("lclsfNm") String lclsfNm, @Param("excludeId") Long excludeId);

	List<WelfareListDTO> getRecommend(Map<String, Object> params);

}