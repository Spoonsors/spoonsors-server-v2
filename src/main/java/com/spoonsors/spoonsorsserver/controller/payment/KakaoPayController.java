package com.spoonsors.spoonsorsserver.controller.payment;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.payment.ApproveRequestPayDto;
import com.spoonsors.spoonsorsserver.entity.payment.PaymentDto;
import com.spoonsors.spoonsorsserver.entity.payment.RequestPayDto;
import com.spoonsors.spoonsorsserver.entity.spon.SponInfoDto;
import com.spoonsors.spoonsorsserver.service.payment.KakaoPayService;
import com.spoonsors.spoonsorsserver.service.spon.SponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/kakao")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    private final SponService sponService;

    @Operation(summary = "카카오페이 결제 요청", description = "후원 물품을 위한 카카오페이 결제 요청을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 요청 성공"),
            @ApiResponse(responseCode = "400", description = "후원 가능한 상태가 아님 또는 결제 요청 실패")
    })
    @PostMapping()
    public ResponseEntity<String> payReady(@RequestBody PaymentDto paymentDto) {

        // 후원 결제 정보 생성
        SponInfoDto sponInfoDto = sponService.getSponInfo(paymentDto.getSpons());

        RequestPayDto requestPayDto = kakaoPayService.payReady(paymentDto.getMemberId(), sponInfoDto);

        if (requestPayDto != null && requestPayDto.getNext_redirect_app_url() != null) {
            sponService.updateTid(requestPayDto.getTid(), paymentDto.getSpons());
            return ResponseEntity.ok(requestPayDto.getNext_redirect_app_url());
        }

        throw new ApiException(ExceptionEnum.PAY01); // 결제 요청 실패에 대한 예외 처리
    }

    @Operation(summary = "결제 완료", description = "카카오페이 결제 완료 후 호출되는 엔드포인트입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 완료"),
            @ApiResponse(responseCode = "400", description = "결제 실패")
    })
    @GetMapping("/complete")
    public ResponseEntity<String> kakaoPaySuccess(@RequestParam("pg_token") String pgToken) {
        ApproveRequestPayDto approveRequestPayDto = kakaoPayService.payApprove(pgToken);
        if (approveRequestPayDto != null) {
            // 스폰 내역 저장
            sponService.applySpon(approveRequestPayDto.getTid(), approveRequestPayDto.getPartner_user_id());
            return ResponseEntity.ok("결제 완료");
        } else {
            throw new ApiException(ExceptionEnum.PAY02); // 결제 실패
        }
    }

    @Operation(summary = "결제 취소", description = "결제 취소 시 호출되는 엔드포인트입니다.")
    @ApiResponse(responseCode = "200", description = "결제 취소")
    @GetMapping("/cancel")
    public ResponseEntity<String> payCancel() {
        return ResponseEntity.ok("결제 취소");
    }

    @Operation(summary = "결제 실패", description = "결제 실패 시 호출되는 엔드포인트입니다.")
    @ApiResponse(responseCode = "400", description = "결제 실패")
    @GetMapping("/fail")
    public ResponseEntity<String> payFail() {
        throw new ApiException(ExceptionEnum.PAY02); // 결제 실패
    }
}