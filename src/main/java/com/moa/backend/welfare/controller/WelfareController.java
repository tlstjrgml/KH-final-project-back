package com.moa.backend.welfare.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.backend.welfare.model.vo.WelfareDetailDTO;
import com.moa.backend.welfare.model.vo.WelfareListDTO;
import com.moa.backend.welfare.service.WelfareService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/welfare")
@CrossOrigin(origins = "http://localhost:5173")
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
    
    @GetMapping("/list")
    public Map<String, Object> getWelfareList(
        @RequestParam(name = "keyword", defaultValue = "") String keyword,
        @RequestParam(name = "lclsfNm", defaultValue = "") String lclsfNm,
        @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        return welfareService.getWelfareList(keyword, lclsfNm, page);
    }
}