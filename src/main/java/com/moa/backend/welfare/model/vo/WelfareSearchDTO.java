package com.moa.backend.welfare.model.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WelfareSearchDTO {
    private List<String> lclsfNm;
    private String region;
    private String age;
    private String income;
    private String job;
    private String keyword;
    private int page;
}