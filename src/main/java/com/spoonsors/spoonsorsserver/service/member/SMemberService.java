package com.spoonsors.spoonsorsserver.service.member;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.SMember;
import com.spoonsors.spoonsorsserver.entity.login.LoginDto;
import com.spoonsors.spoonsorsserver.entity.sMember.SMemberSignUpDto;
import com.spoonsors.spoonsorsserver.loginInfra.JwtTokenProvider;
import com.spoonsors.spoonsorsserver.repository.BMemberRepository;
import com.spoonsors.spoonsorsserver.repository.ISMemberRepository;
import com.spoonsors.spoonsorsserver.repository.IbMemberRepository;
import com.spoonsors.spoonsorsserver.repository.SMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SMemberService {

    private final ISMemberRepository isMemberRepository;
    private final IbMemberRepository ibMemberRepository;
    private final BMemberRepository bMemberRepository;
    private final SMemberRepository sMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder encoder;
    public String signUp(SMemberSignUpDto requestDto) throws Exception {

        //존재하는 아이디
        if (isMemberRepository.findById(requestDto.getId()).isPresent()){
            throw new ApiException(ExceptionEnum.JOIN01);
        }
        if (ibMemberRepository.findById(requestDto.getId()).isPresent()){
            throw new ApiException(ExceptionEnum.JOIN01);
        }
        //존재하는 닉네임
        if (bMemberRepository.findByNickname(requestDto.getNickname()).isPresent()){
            throw new ApiException(ExceptionEnum.JOIN02);
        }
        if (sMemberRepository.findByNickname(requestDto.getNickname()).isPresent()){
            throw new ApiException(ExceptionEnum.JOIN02);
        }
        if (!requestDto.getPwd().equals(requestDto.getPwd_check())){
            throw new ApiException(ExceptionEnum.JOIN03);
        }

        SMember member = isMemberRepository.save(requestDto.toEntity());
        member.encodePassword(passwordEncoder);

        //member.addUserAuthority();
        return member.getMemberId();
    }

    public LoginDto login(Map<String, String> members) {

        SMember sMember = isMemberRepository.findById(members.get("id"))
                .filter(it -> encoder.matches(members.get("pwd"), it.getMemberPw()))   // 암호화된 비밀번호와 비교하도록 수정
                .orElseThrow(() -> new ApiException(ExceptionEnum.LOGIN05)); //아이디와 비밀번호 불일치


        List<String> roles = new ArrayList<>();
        roles.add(sMember.getRole().name());

        LoginDto loginDto = new LoginDto();
        loginDto.setMember_id(sMember.getMemberId());
        loginDto.setMember_name(sMember.getMemberName());
        loginDto.setMember_nickname(sMember.getMemberNickname());
        loginDto.setMember_phoneNumber(sMember.getMemberPhoneNumber());
        loginDto.setMember_profilePath((sMember.getProfilePath()));
        loginDto.setToken(jwtTokenProvider.createToken(sMember.getMemberId(), roles));
        return loginDto;
    }

    public void putToken(Map<String, String> token){
        sMemberRepository.putToken(token);
    }
}
