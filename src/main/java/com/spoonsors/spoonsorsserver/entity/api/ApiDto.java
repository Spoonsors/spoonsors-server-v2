package com.spoonsors.spoonsorsserver.entity.api;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Service
@NoArgsConstructor
public class ApiDto {

    //일련번호
    private String RCP_SEQ;

    //메뉴명
    private String RCP_NM;

    //메뉴 사진(소)
    private String ATT_FILE_NO_MAIN;
    //메뉴 사진(대)
    private String ATT_FILE_NO_MK;
    //요리종류(밥, 반찬, 국&찌개)
    private String RCP_PAT2;

    //조리방법(텍스트, 이미지)
    private List<String> MANUAL = new ArrayList<>();
    private List<String> MANUAL_IMG = new ArrayList<>();

    //열량
    private Float INFO_ENG;

    //탄수화물
    private Float INFO_CAR;
    //단백질
    private Float INFO_PRO;
    //지방
    private Float INFO_FAT;
    //나트륨
    private Float INFO_NA;

    //재료정보
    private String RCP_PARTS_DTLS;



}
