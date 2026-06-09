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
public class Welfare {
    private Long welfareId;
    private String plcyNo;
    private String lclsfNm;
    private String mclsfNm;
    private String mrgSttsCd;
    private String schoolCd;
    private String plcyMajorCd;
    private String jobCd;
    private String sbizCd;
    private Integer sprtTrgtMinAge;
    private Integer sprtTrgtMaxAge;
    private String sprtTrgtAgeLmtYn;
    private Long earnMinAmt;
    private Long earnMaxAmt;
    private String earnEtcCn;
    private String fullData;
}