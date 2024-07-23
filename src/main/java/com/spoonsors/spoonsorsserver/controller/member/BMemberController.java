package com.spoonsors.spoonsorsserver.controller.member;

import com.spoonsors.spoonsorsserver.entity.bMember.BMemberSignUpDto;
import com.spoonsors.spoonsorsserver.entity.login.LoginDto;
import com.spoonsors.spoonsorsserver.service.Image.S3Uploader;
import com.spoonsors.spoonsorsserver.service.member.BMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BMemberController {

    private final BMemberService bMemberService;
    private final S3Uploader s3Uploader;

    @PostMapping(value = "/join/bMember", consumes = {MediaType.APPLICATION_JSON_VALUE, "multipart/form-data"})
    public String join(@RequestPart BMemberSignUpDto dto, @RequestPart(value = "img", required = false) MultipartFile img) throws Exception{

        String url = s3Uploader.upload(img, "certificate");
        String id = bMemberService.signUp(dto,url);

        return id+" 회원가입 완료";
    }

    @PostMapping("/login/bMember")
    public LoginDto login(@RequestBody Map<String, String> member) {
        return bMemberService.login(member);
    }

    @PostMapping("/bMember/putToken")
    public String putToken(@RequestBody Map<String, String> token){
        bMemberService.putToken(token);
        return "완료";
    }

    @GetMapping("/bMember/canPost/{bMemberId}")
    public int canPost(@PathVariable String bMemberId){
        return bMemberService.canPost(bMemberId);
    }
}
