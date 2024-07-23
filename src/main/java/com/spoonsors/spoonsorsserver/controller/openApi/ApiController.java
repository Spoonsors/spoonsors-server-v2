package com.spoonsors.spoonsorsserver.controller.openApi;


import com.spoonsors.spoonsorsserver.entity.api.ApiDto;
import com.spoonsors.spoonsorsserver.service.openApi.ApiService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiController {
    private final ApiService apiService;

    //모든 메뉴 출력
    @GetMapping("/recipe/findAll")
    public List<ApiDto> allAllApi() throws IOException{

        return apiService.findAll();

    }
    //이름으로 메뉴 출력
    @GetMapping("/recipe/findByName")
    public List<ApiDto> findByNameApi(@RequestParam String RCP_NM) throws IOException{
        log.info("RCP_NM={}",RCP_NM);
        return apiService.findByName(RCP_NM.replaceAll(" ",""));

    }
    //밥, 반찬, 국&찌개
    @GetMapping("/recipe/classification")
    public List<ApiDto> classification(@RequestParam String RCP_PAT2) throws Exception {
        log.info("RRCP_PAT2={}",RCP_PAT2);
        return apiService.classification(RCP_PAT2);
    }

}
