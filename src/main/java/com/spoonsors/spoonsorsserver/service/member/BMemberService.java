package com.spoonsors.spoonsorsserver.service.member;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.BMember;
import com.spoonsors.spoonsorsserver.entity.bMember.BMemberSignUpDto;
import com.spoonsors.spoonsorsserver.entity.login.LoginDto;
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
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class BMemberService {

    private final IbMemberRepository ibMemberRepository;
    private final ISMemberRepository isMemberRepository;
    private final BMemberRepository bMemberRepository;
    private final SMemberRepository sMemberRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder encoder;
    public String signUp(BMemberSignUpDto requestDto, String img){

        //이미 존재하는 아이디
        if (ibMemberRepository.findById(requestDto.getId()).isPresent()){
            throw new ApiException(ExceptionEnum.JOIN01);
        }
        if (isMemberRepository.findById(requestDto.getId()).isPresent()){
            throw new ApiException(ExceptionEnum.JOIN01);
        }
        //이미 존재하는 닉네임
        if (bMemberRepository.findByNickname(requestDto.getNickname()).isPresent()){
            throw new ApiException(ExceptionEnum.JOIN02);
        }
        if (sMemberRepository.findByNickname(requestDto.getNickname()).isPresent()){
            throw new ApiException(ExceptionEnum.JOIN02);
        }
        //비밀번호 불일치
        if (!requestDto.getPwd().equals(requestDto.getPwd_check())){
            throw new ApiException(ExceptionEnum.JOIN03);
        }
        requestDto.setCertificate(img);
        BMember member = ibMemberRepository.save(requestDto.toEntity());
        member.encodePassword(passwordEncoder);

        return member.getBMember_id();
    }


    public LoginDto login(Map<String, String> members) {
        BMember bMember = ibMemberRepository.findById(members.get("id"))
                .filter(it -> encoder.matches(members.get("pwd"), it.getBMember_pwd()))   // 암호화된 비밀번호와 비교하도록 수정
                .orElseThrow(() -> new ApiException(ExceptionEnum.LOGIN05)); //아이디와 비밀번호 불일치

        List<String> roles = new ArrayList<>();
        roles.add(bMember.getRole().name());

        LoginDto loginDto = new LoginDto();
        loginDto.setMember_id(bMember.getBMember_id());
        loginDto.setMember_name(bMember.getBMember_name());
        loginDto.setMember_nickname(bMember.getBMember_nickname());
        loginDto.setMember_address(bMember.getBMember_address());
        loginDto.setMember_phoneNumber(bMember.getBMember_phoneNumber());
        loginDto.setMember_profilePath((bMember.getProfile_path()));
        loginDto.setToken(jwtTokenProvider.createToken(bMember.getBMember_id(), roles));
        return loginDto;
    }

    public void putToken(Map<String, String> token){
        bMemberRepository.putToken(token);
    }

    public int canPost(String bMemberId){
        Optional<BMember> optionalBMember = ibMemberRepository.findById(bMemberId);
        return optionalBMember.get().getCan_post();
    }
}
