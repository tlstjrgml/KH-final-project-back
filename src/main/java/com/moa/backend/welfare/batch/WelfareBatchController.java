package com.moa.backend.welfare.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelfareBatchController {

    @Autowired
    private WelfareBatchService welfareBatchService;
//	  //api db저장해놨기 때문에 당분간 쓰지않습니다. 필요할 때 주석해제 후 사용
//    @GetMapping("/api/batch/welfare")
//    public String runBatch(@RequestParam(name = "startPage", defaultValue = "1") int startPage) {
//        welfareBatchService.fetchAndSaveAll(startPage);
//        return "복지 데이터 적재 완료";
//    }
}