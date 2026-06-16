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
public class WelfareDetailDTO {
    private Long welfareId;
    private String lclsfNm;
    private String regionNm;
    private String plcyNm;
    private String sprvsnInstCdNm;
    private String lastMdfcnDt;
    private String sprtTrgtMinAge;
    private String sprtTrgtMaxAge;
    private String ptcpPrpTrgtCn;
    private String earnEtcCn;
    private String plcySprtCn;
    private String aplyYmd;
    private String plcyAplyMthdCn;
    private String srngMthdCn;
    private String aplyUrlAddr;
    private String plcyExplnCn;
    private int wishCount;
}