package com.moa.backend.welfare.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.moa.backend.common.util.page.PageResponse;
import com.moa.backend.common.util.page.Pagination;
import com.moa.backend.welfare.model.mapper.WelfareMapper;
import com.moa.backend.welfare.model.vo.WelfareDetailDTO;
import com.moa.backend.welfare.model.vo.WelfareListDTO;
import com.moa.backend.welfare.model.vo.WelfareSearchDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WelfareService {

    private final WelfareMapper mapper;

    public List<WelfareListDTO> getMainWelfare() {
        return mapper.getMainWelfare();
    }

    public List<WelfareListDTO> getTopten() {
        return mapper.getTopten();
    }

    public WelfareDetailDTO getWelfareDetail(Long id) {
        return mapper.getWelfareDetail(id);
    }

    public List<WelfareListDTO> getRelatedWelfare(String lclsfNm, Long excludeId) {
        return mapper.getRelatedWelfare(lclsfNm, excludeId);
    }

    public PageResponse<WelfareListDTO> getWelfareList(String keyword, List<String> lclsfNm, List<String> region, int ageMin, int ageMax, List<String> income, List<String> job, String sort, int page) {
        int limit = 14;

        WelfareSearchDTO params = new WelfareSearchDTO();
        params.setKeyword(keyword);
        params.setLclsfNm(lclsfNm);
        params.setRegion(region);
        params.setAgeMin(ageMin);
        params.setAgeMax(ageMax);
        params.setIncome(income);
        params.setJob(job);
        params.setSort(sort);
        params.setPage(page);
        params.setLimit(limit);

        int totalItems = mapper.getWelfareCount(params);
        Pagination pagination = new Pagination(params, totalItems);

        List<WelfareListDTO> list = mapper.getWelfareList(params);

        return new PageResponse<>(list, pagination);
    }

    public List<WelfareListDTO> getRecommend(Long memberId, String region, String jobStatus, int incomeLevel) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("region", region);
        params.put("jobStatus", jobStatus);
        params.put("incomeLevel", incomeLevel);
        return mapper.getRecommend(params);
    }
}