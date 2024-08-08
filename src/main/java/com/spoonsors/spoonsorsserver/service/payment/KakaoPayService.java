package com.spoonsors.spoonsorsserver.service.payment;

import com.spoonsors.spoonsorsserver.entity.payment.ApproveRequestPayDto;
import com.spoonsors.spoonsorsserver.entity.payment.RequestPayDto;
import com.spoonsors.spoonsorsserver.entity.spon.SponInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class KakaoPayService {

    @Value("${kakao.cid}")
    private String CID;

    @Value("${kakao.secretKey}")
    private String SECRET_KEY;
    private ApproveRequestPayDto approveRequestPayDto;

    @Transactional
    public RequestPayDto payReady(String memberId, SponInfoDto sponInfoDto) {

        // 카카오가 요구한 결제요청 request값을 담아줍니다.

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", CID); //가맹점 코드. 10자 실제 제휴 후 받아야함
        parameters.put("partner_order_id", "partner_order_id"); // 가맹점의 주문번호
        parameters.put("partner_user_id", memberId);
        parameters.put("item_name", sponInfoDto.getItemName());
        parameters.put("quantity", String.valueOf(sponInfoDto.getQuantity()));
        parameters.put("total_amount", String.valueOf(sponInfoDto.getTotalPrice()));
        parameters.put("tax_free_amount", "0");
        parameters.put("approval_url", "http://localhost:8080/payments/kakao/complete"); // 결제승인시 넘어갈 url
        parameters.put("cancel_url", "http://localhost:8080/payments/kakao/cancel"); // 결제취소시 넘어갈 url
        parameters.put("fail_url", "http://localhost:8080/payments/kakao/fail"); // 결제 실패시 넘어갈 url

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());


        // 외부url요청 통로 열기.
        try {
            RestTemplate template = new RestTemplate();
            String url = "https://open-api.kakaopay.com/online/v1/payment/ready";

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

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", CID); //가맹점 코드. 10자 실제 제휴 후 받아야함
        parameters.put("tid", tid); //결제 고유번호
        parameters.put("partner_order_id", "partner_order_id"); // 가맹점 주문번호, 결제 준비 api요청과 일치 필요
        parameters.put("partner_user_id", sMemberId ); //가맹점 회원 id, 결제 준비 api 요청과 일치
        parameters.put("pg_token", pgToken);

        // 하나의 map안에 header와 parameter값을 담아줌.
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부url 통신
        try {
            RestTemplate template = new RestTemplate();
            String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
            // 보낼 외부 url, 요청 메시지(header,parameter), 처리후 값을 받아올 클래스.
            approveRequestPayDto = template.postForObject(url, requestEntity, ApproveRequestPayDto.class);
            return approveRequestPayDto;
        }catch (RestClientException e) {

            e.printStackTrace();
        }
        return null;

    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","SECRET_KEY "+SECRET_KEY );
        headers.set("Content-type", "application/json");

        return headers;
    }
}
