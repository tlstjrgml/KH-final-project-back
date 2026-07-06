package com.moa.backend.welfare.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.backend.common.config.jwt.CustomUserDetails;
import com.moa.backend.welfare.model.vo.WelfareCategoryDTO;
import com.moa.backend.welfare.model.vo.WelfareDetailDTO;
import com.moa.backend.welfare.model.vo.WelfareListDTO;
import com.moa.backend.welfare.model.vo.WelfareRegionDTO;
import com.moa.backend.welfare.service.WelfareService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/welfare")
@CrossOrigin(origins = {"http://localhost:5173", "http://3.38.12.241"})
@RequiredArgsConstructor
public class WelfareController {

    private final WelfareService welfareService;

    @GetMapping("/main")
    public List<WelfareListDTO> getMainWelfare() {
        return welfareService.getMainWelfare();
    }

    @GetMapping("/topten")
    public List<WelfareListDTO> getTopten() {
        return welfareService.getTopten();
    }

    @GetMapping("/detail/{id}")
    public WelfareDetailDTO getWelfareDetail(@PathVariable("id") Long id) {
        return welfareService.getWelfareDetail(id);
    }

    @GetMapping("/related")
    public List<WelfareListDTO> getRelatedWelfare(
        @RequestParam(name = "lclsfNm") String lclsfNm,
        @RequestParam(name = "excludeId") Long excludeId
    ) {
        return welfareService.getRelatedWelfare(lclsfNm, excludeId);
    }

    @GetMapping("/list")
	    public ResponseEntity<?> getWelfareList(
	        @RequestParam(name = "keyword", defaultValue = "") String keyword,
	        @RequestParam(name = "lclsfNm", defaultValue = "") List<String> lclsfNm,
	        @RequestParam(name = "region", defaultValue = "") List<String> region,
	        @RequestParam(name = "ageMin", defaultValue = "0") int ageMin,
	        @RequestParam(name = "ageMax", defaultValue = "0") int ageMax,
	        @RequestParam(name = "income", defaultValue = "") List<String> income,
	        @RequestParam(name = "job", defaultValue = "") List<String> job,
	        @RequestParam(name = "page", defaultValue = "1") int page,
	        @RequestParam(name = "sort", defaultValue = "") String sort,
	        @RequestParam(name = "school", defaultValue = "") List<String> school
	    ) {
        return ResponseEntity.ok(welfareService.getWelfareList(keyword, lclsfNm, region, ageMin, ageMax, income, job, school, sort, page));
    }
    
    @GetMapping("/recommend")
    public ResponseEntity<List<WelfareListDTO>> getRecommend(
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "jobStatus", required = false) String jobStatus,
            @RequestParam(name = "incomeLevel", required = false, defaultValue = "0") int incomeLevel) {
        Long memberId = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            memberId = ((CustomUserDetails) auth.getPrincipal()).getMemberId();
        }
        return ResponseEntity.ok(welfareService.getRecommend(memberId, region, jobStatus, incomeLevel));
    }
    
    @GetMapping("/regions")
    public List<WelfareRegionDTO> getRegionList(){
    	return welfareService.getRegionList();
    }
    
    @GetMapping("/categories")
    public List<WelfareCategoryDTO> getCategoryList(){
    	return welfareService.getCategoryList();
    }
    
    @GetMapping("/persona")
    public ResponseEntity<Map<String, List<WelfareListDTO>>> getPersona(
            @RequestParam(name = "jobStatus", required = false) String jobStatus,
            @RequestParam(name = "myJobStatus", required = false) String myJobStatus,
            @RequestParam(name = "region", required = false) String region) {

        if (myJobStatus != null && !myJobStatus.isEmpty()) {
            return ResponseEntity.ok(welfareService.getPersonaByDetail(myJobStatus, region));
        } else if (jobStatus != null && !jobStatus.isEmpty()) {
            return ResponseEntity.ok(welfareService.getPersonaByCode(jobStatus));
        } else {
            return ResponseEntity.ok(Map.of());
        }
    }
}