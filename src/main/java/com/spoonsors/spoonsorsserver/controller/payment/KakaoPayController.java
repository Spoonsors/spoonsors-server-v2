package com.spoonsors.spoonsorsserver.controller.payment;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.payment.ApproveRequestPayDto;
import com.spoonsors.spoonsorsserver.entity.payment.PaymentDto;
import com.spoonsors.spoonsorsserver.entity.payment.RequestPayDto;
import com.spoonsors.spoonsorsserver.entity.spon.SponInfoDto;
import com.spoonsors.spoonsorsserver.entity.spon.SponTidDto;
import com.spoonsors.spoonsorsserver.service.payment.KakaoPayService;
import com.spoonsors.spoonsorsserver.service.spon.SponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/payments/kakao")
@RequiredArgsConstructor
public class KakaoPayController {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX = "kakaoPay:";

    private final KakaoPayService kakaoPayService;
    private final SponService sponService;

    @Operation(summary = "카카오페이 결제 요청", description = "후원 물품을 위한 카카오페이 결제 요청을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 요청 성공"),
            @ApiResponse(responseCode = "400", description = "후원 가능한 상태가 아님 또는 결제 요청 실패")
    })
    @PostMapping()
    public ResponseEntity<RequestPayDto> payReady(@RequestBody PaymentDto paymentDto) {

        // 후원 결제 정보 생성
        SponInfoDto sponInfoDto = sponService.getSponInfo(paymentDto.getSpons());

        RequestPayDto requestPayDto = kakaoPayService.payReady(paymentDto.getMemberId(), sponInfoDto);

        if (requestPayDto != null && requestPayDto.getNext_redirect_app_url() != null) {
            // Redis에 저장
            SponTidDto sponTidDto = SponTidDto.builder().tid(requestPayDto.getTid()).spons(paymentDto.getSpons()).memberId(paymentDto.getMemberId()).build();
            String key = PREFIX + requestPayDto.getSMemberId();
            redisTemplate.opsForValue().set(key,sponTidDto, 5, TimeUnit.MINUTES);
            return ResponseEntity.ok(requestPayDto);
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
        String sMemberId = "id1"; //TODO: 로그인 아이디 정보 가져오기
        String key = PREFIX + sMemberId;
        SponTidDto sponTidDto = (SponTidDto) redisTemplate.opsForValue().get(key);

        if (sponTidDto == null) {
            throw new ApiException(ExceptionEnum.PAY03);
        }

        String tid = sponTidDto.getTid();
        ApproveRequestPayDto approveRequestPayDto = kakaoPayService.payApprove(pgToken, tid, sMemberId);
        if (approveRequestPayDto != null) {
            // 스폰 내역 저장
            sponService.applySpon(approveRequestPayDto.getTid(), sponTidDto.getSpons(),approveRequestPayDto.getPartner_user_id());

            // Redis에서 데이터 삭제
            redisTemplate.delete(key);
            return ResponseEntity.ok("결제 완료");
        } else {

            redisTemplate.delete(key);
            throw new ApiException(ExceptionEnum.PAY02); // 결제 실패
        }
    }

    @Operation(summary = "결제 취소", description = "결제 취소 시 호출되는 엔드포인트입니다.")
    @ApiResponse(responseCode = "200", description = "결제 취소")
    @GetMapping("/cancel")
    public ResponseEntity<String> payCancel() {
        String sMemberId = "id1"; //TODO: 로그인 아이디 정보 가져오기
        String key = PREFIX + sMemberId;
        // Redis에서 데이터 삭제
        redisTemplate.delete(key);
        return ResponseEntity.ok("결제 취소");
    }

    @Operation(summary = "결제 실패", description = "결제 실패 시 호출되는 엔드포인트입니다.")
    @ApiResponse(responseCode = "400", description = "결제 실패")
    @GetMapping("/fail")
    public ResponseEntity<String> payFail() {
        String sMemberId = "id1"; //TODO: 로그인 아이디 정보 가져오기
        String key = PREFIX + sMemberId;
        // Redis에서 데이터 삭제
        redisTemplate.delete(key);
        throw new ApiException(ExceptionEnum.PAY02); // 결제 실패
    }
}