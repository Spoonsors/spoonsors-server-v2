package com.spoonsors.spoonsorsserver.controller.spon;


import com.spoonsors.spoonsorsserver.entity.spon.SponDto;
import com.spoonsors.spoonsorsserver.service.spon.SponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SponController {
    private final SponService sponService;

    /*
    //후원 신청
    @PostMapping("/applySpon/{spon_id}/{sMemberId}")
    public ResponseEntity<?> applySpon(@PathVariable Long spon_id, @PathVariable String sMemberId){

        String txt = sponService.applySpon(spon_id, sMemberId);
        ResponseEntity<String> result = ResponseEntity.status(HttpStatus.OK).body(txt);

        //잘못된 신청일 경우
        if(txt.equals("이미 후원이 완료된 물품입니다.")) {
            result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(txt);
        }
        return result;
    }

     */

    //후원 내역 조회
    @GetMapping("/getSponList/{sMemberId}")
    public List<SponDto> getSponList(@PathVariable String sMemberId){
       return sponService.getSponList(sMemberId);
    }
}
