package com.spoonsors.spoonsorsserver.service.openApi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.api.ApiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApiService {

    private String serviceKey = "fa901cadedf748768100";
    private String serviceId = "COOKRCP01";

    //모든 데이터 출력
    public List<ApiDto> findAll() throws IOException {

        String urlStr = "http://openapi.foodsafetykorea.go.kr/api/" +
                serviceKey + "/" + serviceId + "/json/" + "1/500";

        return init(urlStr);

    }

    // 메뉴 이름으로 레시피 검색
    public List<ApiDto> findByName(String RCP_NM) throws IOException {
        String urlStr = "http://openapi.foodsafetykorea.go.kr/api/" +
                serviceKey + "/" + serviceId + "/json/" + "1/20/RCP_PAT2=반찬&RCP_NM=" + RCP_NM;
        List<ApiDto> arr = init(urlStr);
        log.info("arr={}",arr.get(0).getRCP_NM());
        if (arr != null) {
            return arr;
        }
        urlStr = "http://openapi.foodsafetykorea.go.kr/api/" +
                serviceKey + "/" + serviceId + "/json/" + "1/5/RCP_PAT2=밥&RCP_NM=" + RCP_NM;
        arr = init(urlStr);
        log.info("arr={}",arr.get(0).getRCP_NM());
        if (arr != null) {
            return arr;
        }
        urlStr = "http://openapi.foodsafetykorea.go.kr/api/" +
                serviceKey + "/" + serviceId + "/json/" + "1/10/RCP_PAT2=국&RCP_NM=" + RCP_NM;
        arr = init(urlStr);
        log.info("arr={}",arr.get(0).getRCP_NM());
        if (arr != null) {
            return arr;
        }
        throw new ApiException(ExceptionEnum.OPENAPI03);


    }

    // 밥, 국&찌개 반찬 검색
    public List<ApiDto> classification(String RCP_PAT2) throws Exception {
        String urlStr;
        if (Objects.equals(RCP_PAT2, "반찬")) {
            urlStr = "http://openapi.foodsafetykorea.go.kr/api/" +
                    serviceKey + "/" + serviceId + "/json/" + "1/20/RCP_PAT2=" + RCP_PAT2;
        } else if (Objects.equals(RCP_PAT2, "밥")) {
            urlStr = "http://openapi.foodsafetykorea.go.kr/api/" +
                    serviceKey + "/" + serviceId + "/json/" + "1/5/RCP_PAT2=" + RCP_PAT2;
        } else if (Objects.equals(RCP_PAT2, "국") || Objects.equals(RCP_PAT2, "찌개")) {
            urlStr = "http://openapi.foodsafetykorea.go.kr/api/" +
                    serviceKey + "/" + serviceId + "/json/" + "1/10/RCP_PAT2=" + RCP_PAT2;
        } else {
            // 입력값이 잘 못 되었을 경우 에러
            throw new ApiException(ExceptionEnum.OPENAPI01);
        }
        return init(urlStr);


    }

    public List<ApiDto> init(String urlStr) throws IOException {

        StringBuilder result = new StringBuilder();

        URL url = new URL(urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        String returnLine;

        while ((returnLine = br.readLine()) != null) {
            result.append(returnLine + "\n");
        }
        urlConnection.disconnect();
        ArrayList<ApiDto> menuList = new ArrayList<>();

        //Json 객체 생성
        JSONObject jo;
        //JSON 파싱 객체 생성
        JSONParser jsonParser = new JSONParser();
        //파싱할 String (Controller에서 호출해 값이 저장된 StringBuilder result)을
        //JSON 객체로 파서를 통해 저장
        JSONObject jsonObj = null;
        try {
            jsonObj = (JSONObject) jsonParser.parse(result.toString());
        } catch (ParseException e) {
            throw new ApiException(ExceptionEnum.OPENAPI04); //파싱 불가 에러
        }
        //데이터 분해 단계

        //response를 받아옴
        JSONObject parseResponse = (JSONObject) jsonObj.get("COOKRCP01");
        //parseResponse에는 response 내부의 데이터들이 들어있음
        JSONObject errResponse = (JSONObject) parseResponse.get("RESULT");
        String err = (String) errResponse.get("CODE");

        if (err.equals("INFO-300")) {
            log.info("parseResponse={}",parseResponse);
            log.info("err={}",err);
            throw new ApiException(ExceptionEnum.OPENAPI02); // 유효 호출건수를 이미 초과하셨습니다.
        } else if (err.equals("INFO-200")) {
            return null;
        }

        //items 안쪽의 데이터는 [] 즉 배열의 형태이기에 json 배열로 받아온다.
        JSONArray array = (JSONArray) parseResponse.get("row");
        //매핑
        for (int i = 0; i < array.size(); i++) {
            ApiDto apiDto = new ApiDto();
            jo = (JSONObject) array.get(i);
            apiDto.setRCP_SEQ(jo.get("RCP_SEQ").toString());
            apiDto.setRCP_NM(jo.get("RCP_NM").toString());
            apiDto.setATT_FILE_NO_MAIN(jo.get("ATT_FILE_NO_MAIN").toString());
            apiDto.setATT_FILE_NO_MK(jo.get("ATT_FILE_NO_MK").toString());
            apiDto.setRCP_PAT2(jo.get("RCP_PAT2").toString());
            apiDto.setINFO_ENG(Float.parseFloat(jo.get("INFO_ENG").toString()));
            apiDto.setINFO_CAR(Float.parseFloat(jo.get("INFO_CAR").toString()));
            apiDto.setINFO_PRO(Float.parseFloat(jo.get("INFO_PRO").toString()));
            apiDto.setINFO_FAT(Float.parseFloat(jo.get("INFO_FAT").toString()));
            apiDto.setINFO_NA(Float.parseFloat(jo.get("INFO_NA").toString()));
            apiDto.setRCP_PARTS_DTLS(jo.get("RCP_PARTS_DTLS").toString());
            List<String> MANUAL_List = new ArrayList<>();
            List<String> MANUAL_IMG_List = new ArrayList<>();
            for (int j = 1; j < 21; j++) {
                String MANUAL = "";
                String MANUAL_IMG = "";
                if (j < 10) {
                    MANUAL = "MANUAL0" + j;
                    MANUAL_IMG = "MANUAL_IMG0" + j;

                } else {
                    MANUAL = "MANUAL" + j;
                    MANUAL_IMG = "MANUAL_IMG" + j;
                }
                MANUAL_List.add(jo.get(MANUAL).toString());
                MANUAL_IMG_List.add(jo.get(MANUAL_IMG).toString());

            }
            apiDto.setMANUAL(MANUAL_List);
            apiDto.setMANUAL_IMG(MANUAL_IMG_List);

            menuList.add(apiDto);
        }

        return menuList;
    }
}
