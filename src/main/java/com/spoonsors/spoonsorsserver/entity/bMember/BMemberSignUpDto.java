package com.spoonsors.spoonsorsserver.entity.bMember;

import com.spoonsors.spoonsorsserver.entity.BMember;
import com.spoonsors.spoonsorsserver.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
public class BMemberSignUpDto {

    @NotBlank(message = "아이디를 입력해주세요")
    private String id;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$",
            message = "비밀번호는 8~15 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String pwd;

    private String pwd_check;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "생년월일을 입력해주세요.(예:20010904)")
    private LocalDate birth;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min=2, message = "2글자 이상으로 입력해주세요.")
    private String nickname;

    @NotBlank(message = "휴대전화 번호를 입력해주세요.")
    private String phoneNumber;

    private String address;

    private String certificate;

    private String token;

    private String profilePath;


    @Builder
    public BMember toEntity(){
        return BMember.builder()
                .bMemberId(id)
                .bMemberPw(pwd)
                .bMemberName(name)
                .bMemberBirth(birth)
                .bMemberNickname(nickname)
                .bMemberPhoneNumber(phoneNumber)
                .bMemberAddress(address)
                .bMemberCertificate(certificate)
                .role(Role.ROLE_USER)
                .token(token)
                .profilePath(profilePath)
                .isVerified(false)
                .canPost(false)
                .build();
    }
}
