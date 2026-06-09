package com.moa.backend.welfare.model.vo;

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
public class WelfareListDTO {
    private Long welfareId;
    private String lclsfNm;
    private String plcyNm;
    private String aplyYmd;
    private String sprvsnInstCdNm;
}