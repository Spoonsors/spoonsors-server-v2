package com.spoonsors.spoonsorsserver.controller.authorize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spoonsors.spoonsorsserver.entity.authorize.MessageDto;
import com.spoonsors.spoonsorsserver.entity.authorize.SmsResponseDto;
import com.spoonsors.spoonsorsserver.service.authorize.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/sms/send")
    public SmsResponseDto sendSms(@RequestBody MessageDto messageDto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {

        SmsResponseDto responseDto = smsService.sendSms(messageDto);

        return responseDto;
    }

    @PostMapping("/sms/verify")
    public String verifySms(@RequestBody Map<String,String> verification) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        String verfication;

        verfication = smsService.verifySms(verification.get("phoneNum"), verification.get("code"));
        return verfication;
    }
}