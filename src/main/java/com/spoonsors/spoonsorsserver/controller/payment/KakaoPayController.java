package com.spoonsors.spoonsorsserver.controller.payment;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.payment.ApproveRequestPayDto;
import com.spoonsors.spoonsorsserver.entity.payment.PaymentDto;
import com.spoonsors.spoonsorsserver.service.payment.KakaoPayService;
import com.spoonsors.spoonsorsserver.service.spon.SponService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    private final SponService sponService;

    // 카카오페이결제 요청
    @PostMapping("/sMember/kakaoPay")
    public ResponseEntity<?> payReady(@RequestBody PaymentDto paymentDto){
        List<Long> sponList = paymentDto.getSpon_list();
        String txt="";

        //후원 가능한 상태인지 확인
        for (Long spon_id : sponList) {
            txt = sponService.checkSpon(spon_id);
            if (txt.equals("이미 후원이 완료된 물품입니다.")) {
                throw new ApiException(ExceptionEnum.SPON01);
            }
        }

        String link= kakaoPayService.payReady(paymentDto);
        if(link.equals("결제 요청 실패")){
            throw new ApiException(ExceptionEnum.PAY01); //결제 요청 실패
        }
        return ResponseEntity.status(HttpStatus.OK).body(link);


    }

    //결제 완료
    @GetMapping("/sMember/kakaoPay/completed")
    public ResponseEntity<?>  kakaoPaySuccess(@RequestParam("pg_token") String pg_token) {
        ApproveRequestPayDto approveRequestPayDto = kakaoPayService.payApprove(pg_token);
        if(approveRequestPayDto!=null){
            //스폰 내역 저장
            sponService.applySpon(approveRequestPayDto.getTid(), approveRequestPayDto.getPartner_user_id());
            return ResponseEntity.status(HttpStatus.OK).body("결제 완료");
        }else{
            throw new ApiException(ExceptionEnum.PAY02); //결제 실패
        }
    }

    // 결제 취소시 실행 url
    @GetMapping("/sMember/kakaoPay/cancel")
    public String payCancel() {
        return "결제 취소"; //todo 수정
    }

    // 결제 실패시 실행 url
    @GetMapping("/sMember/kakaoPay/fail")
    public String payFail() {
        throw new ApiException(ExceptionEnum.PAY02); //결제 실패
    }
}
