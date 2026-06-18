package com.moa.backend.welfare.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

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

    public Map<String, Object> getWelfareList(String keyword, List<String> lclsfNm, List<String> region, int ageMin, int ageMax, List<String> income, List<String> job, int page) {
        int pageSize = 14;
        int offset = (page - 1) * pageSize;

        WelfareSearchDTO params = new WelfareSearchDTO();
        params.setKeyword(keyword);
        params.setLclsfNm(lclsfNm);
        params.setRegion(region);
        params.setAgeMin(ageMin);
        params.setAgeMax(ageMax);
        params.setIncome(income);
        params.setJob(job);
        params.setPage(offset);

        List<WelfareListDTO> list = mapper.getWelfareList(params);
        int total = mapper.getWelfareCount(params);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("totalPages", (int) Math.ceil((double) total / pageSize));
        return result;
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