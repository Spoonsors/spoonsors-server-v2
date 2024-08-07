package com.spoonsors.spoonsorsserver.service.member;

import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.SMember;
import com.spoonsors.spoonsorsserver.entity.login.LoginDto;
import com.spoonsors.spoonsorsserver.entity.sMember.SMemberSignUpDto;
import com.spoonsors.spoonsorsserver.entity.sMember.TokenUpdateReqDto;
import com.spoonsors.spoonsorsserver.repository.ISMemberRepository;
import com.spoonsors.spoonsorsserver.repository.IbMemberRepository;
import com.spoonsors.spoonsorsserver.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SMemberService {

    private final ISMemberRepository isMemberRepository;
    private final IbMemberRepository ibMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder encoder;

    @Transactional
    public String signUp(SMemberSignUpDto requestDto) throws Exception {

        //존재하는 아이디
        if (isMemberRepository.findById(requestDto.getId()).isPresent()){
            throw new ApiException(ExceptionEnum.JOIN01);
        }
        if (ibMemberRepository.findById(requestDto.getId()).isPresent()){
            throw new ApiException(ExceptionEnum.JOIN01);
        }
        //존재하는 닉네임
        if (ibMemberRepository.findByMemberNickname(requestDto.getNickname()).isPresent()){
            throw new ApiException(ExceptionEnum.JOIN02);
        }
        if (isMemberRepository.findByMemberNickname(requestDto.getNickname()).isPresent()){
            throw new ApiException(ExceptionEnum.JOIN02);
        }
        if (!requestDto.getPwd().equals(requestDto.getPwd_check())){
            throw new ApiException(ExceptionEnum.JOIN03);
        }

        SMember member = requestDto.toEntity();
        member.encodePassword(passwordEncoder);

        // 저장
        member = isMemberRepository.save(member);

        return member.getMemberId();
    }

    public LoginDto login(Map<String, String> members) {

        SMember sMember = isMemberRepository.findById(members.get("id"))
                .filter(it -> encoder.matches(members.get("pwd"), it.getMemberPw()))   // 암호화된 비밀번호와 비교하도록 수정
                .orElseThrow(() -> new ApiException(ExceptionEnum.LOGIN05)); //아이디와 비밀번호 불일치

        List<String> roles = new ArrayList<>();
        roles.add(sMember.getRole().name());

        return LoginDto.builder()
                .id(sMember.getMemberId())
                .name(sMember.getMemberName())
                .nickname(sMember.getMemberNickname())
                .phoneNumber(sMember.getMemberPhoneNumber())
                .profilePath(sMember.getProfilePath())
                .token(jwtTokenProvider.createToken(sMember.getMemberId(), roles)).build();
    }

    @Transactional
    public void putToken(TokenUpdateReqDto token){
        isMemberRepository.updateToken(token.getMemberId(), token.getToken());
    }
}
