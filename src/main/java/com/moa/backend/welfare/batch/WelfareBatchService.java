package com.moa.backend.welfare.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.moa.backend.welfare.model.mapper.WelfareMapper;
import com.moa.backend.welfare.model.vo.Welfare;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WelfareBatchService {

    @Autowired
    private WelfareMapper welfareMapper;

    @Value("${youth.api.key}")
    private String apiKey;

    private static final String API_URL = "https://www.youthcenter.go.kr/go/ythip/getPlcy";
    private static final int PAGE_SIZE = 10;

    public void fetchAndSaveAll(int startPage) {

        System.out.println("API KEY: " + apiKey);
        System.out.println("startPage: " + startPage);

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        int pageNum = startPage;

        try {
            while (true) {
                String url = API_URL + "?apiKeyNm=" + apiKey
                        + "&pageNum=" + pageNum
                        + "&pageSize=" + PAGE_SIZE;

                String response = restTemplate.getForObject(url, String.class);
                JsonNode root = objectMapper.readTree(response);
                JsonNode list = root.path("result").path("youthPolicyList");

                if (list.isMissingNode() || list.size() == 0) break;

                for (JsonNode node : list) {
                    try {
                        Welfare vo = new Welfare();
                        vo.setPlcyNo(node.path("plcyNo").asText());
                        vo.setLclsfNm(node.path("lclsfNm").asText());
                        vo.setMclsfNm(node.path("mclsfNm").asText());
                        vo.setMrgSttsCd(node.path("mrgSttsCd").asText());
                        vo.setSchoolCd(node.path("schoolCd").asText());
                        vo.setPlcyMajorCd(node.path("plcyMajorCd").asText());
                        vo.setJobCd(node.path("jobCd").asText());
                        vo.setSbizCd(node.path("sbizCd").asText());
                        vo.setSprtTrgtMinAge(node.path("sprtTrgtMinAge").asInt());
                        vo.setSprtTrgtMaxAge(node.path("sprtTrgtMaxAge").asInt());
                        vo.setSprtTrgtAgeLmtYn(node.path("sprtTrgtAgeLmtYn").asText());
                        vo.setEarnMinAmt(node.path("earnMinAmt").asLong());
                        vo.setEarnMaxAmt(node.path("earnMaxAmt").asLong());
                        vo.setEarnEtcCn(node.path("earnEtcCn").asText());
                        vo.setFullData(node.toString());
                        welfareMapper.mergeWelfare(vo);
                    } catch (Exception e) {
                        System.out.println("스킵: " + node.path("plcyNo").asText() + " - " + e.getMessage());
                    }
                }

                int totCount = root.path("result").path("pagging").path("totCount").asInt();
                System.out.println("pageNum: " + pageNum + " / 총: " + totCount);
                if ((long) pageNum * PAGE_SIZE >= totCount) break;
                pageNum++;
            }
            System.out.println("복지 데이터 적재 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}