package com.spoonsors.spoonsorsserver.service.payment;

import com.spoonsors.spoonsorsserver.entity.payment.ApproveRequestPayDto;
import com.spoonsors.spoonsorsserver.entity.payment.RequestPayDto;
import com.spoonsors.spoonsorsserver.entity.spon.SponInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

    private ApproveRequestPayDto approveRequestPayDto;

    @Transactional
    public RequestPayDto payReady(String memberId, SponInfoDto sponInfoDto) {

        // 카카오가 요구한 결제요청 request값을 담아줍니다.

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", "TC0ONETIME"); //가맹점 코드. 10자 실제 제휴 후 받아야함
        parameters.add("partner_order_id", "partner_order_id"); // 가맹점의 주문번호
        parameters.add("partner_user_id", memberId);
        parameters.add("item_name", sponInfoDto.getItemName());
        parameters.add("quantity", String.valueOf(sponInfoDto.getQuantity()));
        parameters.add("total_amount", String.valueOf(sponInfoDto.getTotalPrice()));
        parameters.add("tax_free_amount", "0");
        parameters.add("approval_url", "https://localhost:8080/payments/kakao/complete"); // 결제승인시 넘어갈 url
        parameters.add("cancel_url", "https://localhost:8080/payments/kakao/cancel"); // 결제취소시 넘어갈 url
        parameters.add("fail_url", "https://localhost:8080/payments/kakao/fail"); // 결제 실패시 넘어갈 url

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        // 외부url요청 통로 열기.
        try {
            RestTemplate template = new RestTemplate();
            String url = "https://kapi.kakao.com/v1/payment/ready";

            RequestPayDto requestPayDto = template.postForObject(url, requestEntity, RequestPayDto.class);

            if (requestPayDto != null) {
                requestPayDto.setSMemberId(memberId);
            }

            return requestPayDto;

        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 결제 승인요청 메서드
    @Transactional
    public ApproveRequestPayDto payApprove( String pgToken, String tid, String sMemberId) {
        log.info("----------------------------payApprove" );

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", "TC0ONETIME"); //가맹점 코드. 10자 실제 제휴 후 받아야함
        parameters.add("tid", tid); //결제 고유번호
        parameters.add("partner_order_id", "partner_order_id"); // 가맹점 주문번호, 결제 준비 api요청과 일치 필요
        parameters.add("partner_user_id", sMemberId ); //가맹점 회원 id, 결제 준비 api 요청과 일치
        parameters.add("pg_token", pgToken);

        // 하나의 map안에 header와 parameter값을 담아줌.
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부url 통신
        try {
            RestTemplate template = new RestTemplate();
            String url = "https://kapi.kakao.com/v1/payment/approve";
            // 보낼 외부 url, 요청 메시지(header,parameter), 처리후 값을 받아올 클래스.
            approveRequestPayDto = template.postForObject(url, requestEntity, ApproveRequestPayDto.class);
            log.info("----------------------------approveRequestPayDto={}", approveRequestPayDto.getTid() );
            return approveRequestPayDto;
        }catch (RestClientException e) {

            e.printStackTrace();
        }
        return null;

    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","KakaoAK 942dc22415c337900d9f5159a808d612" );
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return headers;
    }
}
