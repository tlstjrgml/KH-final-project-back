package com.moa.backend.welfare.model.vo;

import java.util.List;
import com.moa.backend.common.util.page.PageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class WelfareSearchDTO extends PageRequest {
    private List<String> lclsfNm;
    private List<String> region;
    private List<String> income;
    private List<String> job;
    private String keyword;
    private String sort;
    private int ageMin;
    private int ageMax;
}