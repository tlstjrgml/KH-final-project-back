package com.moa.backend.welfare.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.moa.backend.common.util.page.PageResponse;
import com.moa.backend.common.util.page.Pagination;
import com.moa.backend.welfare.model.mapper.WelfareMapper;
import com.moa.backend.welfare.model.vo.WelfareCategoryDTO;
import com.moa.backend.welfare.model.vo.WelfareDetailDTO;
import com.moa.backend.welfare.model.vo.WelfareListDTO;
import com.moa.backend.welfare.model.vo.WelfareRegionDTO;
import com.moa.backend.welfare.model.vo.WelfareSearchDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WelfareService {

    private final WelfareMapper mapper;

    private static final Map<String, String[]> PERSONA_CATEGORY_MAP = Map.of(
        "대학생", new String[]{"교육", "금융･복지･문화"},
        "취준생", new String[]{"일자리", "참여･기반"},
        "사회초년생", new String[]{"주거", "금융･복지･문화"}
    );

    private static final List<String> ALL_CATEGORIES =
        List.of("주거", "일자리", "교육", "참여･기반", "금융･복지･문화");


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

    public PageResponse<WelfareListDTO> getWelfareList(String keyword, List<String> lclsfNm, List<String> region,
            int ageMin, int ageMax, List<String> income, List<String> job, List<String> school, String sort, int page) {
        int limit = 14;
        WelfareSearchDTO params = new WelfareSearchDTO();
        params.setKeyword(keyword);
        params.setLclsfNm(lclsfNm);
        params.setRegion(region);
        params.setAgeMin(ageMin);
        params.setAgeMax(ageMax);
        params.setIncome(income);
        params.setJob(job);
        params.setSchool(school);
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

        boolean hasWish = memberId != null && mapper.countWish(memberId) > 0;
        params.put("memberId", memberId);
        params.put("hasWish", hasWish);
        params.put("region", resolveRegionCode(region));
        params.putAll(resolveJobStatusParams(jobStatus));
        params.put("earnCndSeCd", resolveEarnCode(incomeLevel));

        return mapper.getRecommend(params);
    }


    public Map<String, List<WelfareListDTO>> getPersonaByCode(String jobStatus) {
        String[] categories = PERSONA_CATEGORY_MAP.get(jobStatus);
        if (categories == null) return new LinkedHashMap<>();

        Map<String, Object> baseParams = resolveJobStatusParams(jobStatus);
        return buildPersonaResult(categories, baseParams, "getPersonaByCode");
    }

    public Map<String, List<WelfareListDTO>> getPersonaByDetail(String jobStatus, String region) {
        Map<String, Object> baseParams = resolveJobStatusParams(jobStatus);
        baseParams.put("region", resolveRegionCode(region));

        String[] categories = ALL_CATEGORIES.toArray(new String[0]);
        return buildPersonaResult(categories, baseParams, "getPersonaByDetail");
    }

    private Map<String, List<WelfareListDTO>> buildPersonaResult(String[] categories, Map<String, Object> baseParams, String queryId) {
        Map<String, List<WelfareListDTO>> result = new LinkedHashMap<>();
        for (String category : categories) {
            Map<String, Object> params = new HashMap<>(baseParams);
            params.put("lclsfNm", category);
            List<WelfareListDTO> items = "getPersonaByCode".equals(queryId)
                ? mapper.getPersonaByCode(params)
                : mapper.getPersonaByDetail(params);
            result.put(category, items);
        }
        return result;
    }

    public List<WelfareRegionDTO> getRegionList() {
        return mapper.getRegionList();
    }

    public List<WelfareCategoryDTO> getCategoryList() {
        return mapper.getCategoryList();
    }

    // 공통부분 묶기

    private String resolveRegionCode(String region) {
        return region != null ? mapper.getRegionCode(region) : null;
    }

    private Map<String, Object> resolveJobStatusParams(String jobStatus) {
        Map<String, Object> params = new HashMap<>();
        if ("대학생".equals(jobStatus)) {
            params.put("schoolCd", "0049005");
        } else if ("취준생".equals(jobStatus)) {
            params.put("jobCd", "0013003");
        } else if ("사회초년생".equals(jobStatus)) {
            params.put("jobCd", "0013001");
        }
        return params;
    }

    private String resolveEarnCode(int incomeLevel) {
        return switch (incomeLevel) {
            case 0 -> "0043001";
            case 1 -> "0043002";
            case 2 -> "0043003";
            default -> null;
        };
    }
}