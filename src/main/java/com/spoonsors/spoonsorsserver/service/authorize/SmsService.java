package com.spoonsors.spoonsorsserver.service.authorize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.authorize.Auth;
import com.spoonsors.spoonsorsserver.entity.authorize.MessageDto;
import com.spoonsors.spoonsorsserver.entity.authorize.SmsRequestDto;
import com.spoonsors.spoonsorsserver.entity.authorize.SmsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@PropertySource("classpath:application.properties")
@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {


    //private final SmsRepository smsRepository;

    private final AuthService authService;
    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    private String phone;

    public SmsResponseDto sendSms(MessageDto messageDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String smsConfirmNum = createSmsKey(messageDto.getTo());
        log.info("사용자에게 전송한 인증코드={}",smsConfirmNum);
        // 현재시간
        String time = Long.toString(System.currentTimeMillis());

        // 헤더세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time);
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", getSignature(time)); // signature 서명

        List<MessageDto> messages = new ArrayList<>();
        messages.add(messageDto);

        // api 요청 양식에 맞춰 세팅
        SmsRequestDto request = SmsRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(phone)
                .content("[모두의 한끼] 인증번호 [" + smsConfirmNum + "]를 입력해주세요")
                .messages(messages)
                .build();

        //request를 json형태로 body로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        // body와 header을 합친다
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        //restTemplate를 통해 외부 api와 통신

        SmsResponseDto smsResponseDto = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages"), httpBody, SmsResponseDto.class);
        SmsResponseDto responseDto = new SmsResponseDto(smsConfirmNum);

        /*Sms sms = new Sms(messageDto.getTo(), responseDto.getSmsConfirmNum());
        smsRepository.save(sms);*/

        return smsResponseDto;
    }

    // 전달하고자 하는 데이터를 암호화해주는 작업
    public String getSignature(String time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + this.serviceId + "/messages";
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(time)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    // 5자리의 난수를 조합을 통해 인증코드 만들기
    public String createSmsKey(String phoneNum) {

        Auth auth = new Auth();

        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 5; i++) { // 인증코드 5자리
            key.append((rnd.nextInt(10)));
        }

        auth.setPhoneNum(phoneNum);
        auth.setCode(key.toString());
        authService.addAuth(auth);
        log.info("authService.addAuth(auth)={}",authService.addAuth(auth));
        log.info("난수 생성 하기위해 받은 폰 번호={}",phoneNum);

        return key.toString();
    }
    public boolean isValidToken(String phoneNum,String verificationCode){
        return authService.getAuthById(phoneNum).getCode().equals(verificationCode);
    }
    public String verifySms(String phoneNum,String verificationCode){
        log.info("사용자가 전송한 번호={}",phoneNum);
        log.info("사용자가 전송한 인증코드={}",verificationCode);

        if(authService.getAuthById(phoneNum) == null){
            // 세션 만료되었을시 에러
            throw new ApiException(ExceptionEnum.AUTHORIZE02);
        }
        if(!isValidToken(phoneNum,verificationCode)){
            // 인증 번호 맞지 않을 시 에러
            throw new ApiException(ExceptionEnum.AUTHORIZE01);
        }
        return "인증완료 되었습니다.";
    }
}