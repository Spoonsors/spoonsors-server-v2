package com.spoonsors.spoonsorsserver.controller.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.service.authorize.SmsService;
import com.spoonsors.spoonsorsserver.service.member.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JoinController {
    private final JoinService joinService;
    private final SmsService smsService;

    @GetMapping("/join/checkId")
    public String checkId(@RequestParam String id){
        String txt=joinService.checkId(id);
        return txt;
    }

    @GetMapping("/join/checkNickname")
    public String checkNickname(@RequestParam String nickname){
        String txt=joinService.checkNickname(nickname);
        return txt;
    }


    //카카오 로그인으로 회원가입 및 로그인
    @GetMapping("/join/kakao")
    public String kakaoLogin(@RequestParam String code) throws Throwable {

        String accessToken= joinService.getAccessToken(code);
        HashMap<String, String> info= joinService.getUserInfo(accessToken);

        return joinService.loginOrJoin(info);
    }

    // 아이디 찾기(이름 번호 match)
//    @PostMapping("/join/matchId")
//    public String findId(HttpServletRequest request, @RequestBody Map<String,String> find) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
//        return joinService.findId(request, find.get("name"),find.get("phoneNum"));
//    }
    @PostMapping("/join/matchId")
    public String findId(@RequestBody Map<String,String> find) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        return joinService.findId(find.get("name"),find.get("phoneNum"));
    }
    //아이디 찾기 인증확인
    @PostMapping("/join/findId")
    public String verifyFindId(@RequestBody Map<String,String> verification) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        String verify;
        verify = smsService.verifySms(verification.get("phoneNum"), verification.get("code"));
        if(verify == null) {
            throw new ApiException(ExceptionEnum.AUTHORIZE01); //인증번호 불일치
        }else{
            verify = joinService.verifyFindId(verification.get("name"),verification.get("phoneNum"));
            return verify;
        }
    }
    // 비밀번호 변경(아이디 존재 확인)
    @GetMapping("/join/matchId/{memberId}")
    public String matchId(@PathVariable String memberId){ return joinService.matchId(memberId);}
    // 비밀번호 변경(문자인증(아이디, 이름, 폰 번호 일치해야함))
    @PostMapping("/join/matchPwd")
    public String authorizePwd(@RequestBody Map<String,String> find) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        return joinService.authorizePwd(find.get("id"), find.get("name"),find.get("phoneNum"));
    }
    // 비밀번호 변경(인증 확인)
    @PostMapping("/join/verifyPwd")
    public String verifyPwd(@RequestBody Map<String,String> verification){
        String verify;
        verify = smsService.verifySms(verification.get("phoneNum"), verification.get("code"));
        if(verify == null) {
            throw new ApiException(ExceptionEnum.AUTHORIZE01); //인증번호 불일치
        }else{
            verify = "인증완료 되었습니다.";
            return verify;
        }
    }
    // 비밀번호 변경(변경)
    @PostMapping("/join/changePwd")
    public String changePwd(@RequestBody Map<String,String> verification){
        String result;
        result = joinService.changePwd(verification);
        if(result == null){
            throw new ApiException(ExceptionEnum.LOGIN04); //아이디 비번 불일치
        }else {
            return result;
        }
    }
}
