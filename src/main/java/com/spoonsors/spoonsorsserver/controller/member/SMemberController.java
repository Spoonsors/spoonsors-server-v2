package com.spoonsors.spoonsorsserver.controller.member;

import com.spoonsors.spoonsorsserver.entity.login.LoginDto;
import com.spoonsors.spoonsorsserver.entity.sMember.SMemberSignUpDto;
import com.spoonsors.spoonsorsserver.service.member.SMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SMemberController {
    private final SMemberService sMemberService;

    @PostMapping("/join/sMember")
    public String join(@RequestBody SMemberSignUpDto dto) throws Exception{
        String id=sMemberService.signUp(dto);

        return id+" 회원가입 완료";
    }

    @PostMapping("/login/sMember")
    public LoginDto login(@RequestBody Map<String, String> member) {
        return sMemberService.login(member);
    }

    @PostMapping("/sMember/putToken")
    public String putToken(@RequestBody Map<String, String> token){
        sMemberService.putToken(token);
        return "완료";
    }
}
