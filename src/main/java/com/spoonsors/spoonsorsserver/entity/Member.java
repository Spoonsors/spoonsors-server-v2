package com.spoonsors.spoonsorsserver.entity;

import com.spoonsors.spoonsorsserver.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public abstract class Member extends BaseEntity {
    @Id
    @Column(length = 100, nullable = false)
    private String memberId;

    @Column(name = "member_pw", nullable = false, length = 100)
    private String memberPw;

    @Column(name = "member_nickname", nullable = false, length = 100, unique = true)
    private String memberNickname;

    @Column(name = "member_name", nullable = false, length = 100)
    private String memberName;

    @Column(name = "member_phone_number", nullable = false, length = 100)
    private String memberPhoneNumber;

    @Column(name = "token", length = 255)
    private String token;

    @Column(name = "profile_path", nullable = false, length = 255)
    private String profilePath;

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.memberPw = passwordEncoder.encode(memberPw);
    }
}
