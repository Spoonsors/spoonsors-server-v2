package com.spoonsors.spoonsorsserver.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(
        name="bMember",
        uniqueConstraints={
                @UniqueConstraint(
                        name="phoneAndName",
                        columnNames={"bMember_name", "bMember_phoneNumber"}
                )
        }
)
public class BMember extends BaseTime{
    @Id
    @Column(name="b_member_id", length = 100, nullable = false)
    private String bMemberId;

    @Column(name="b_member_pw", length = 100, nullable = false)
    private String bMemberPw;

    @Column(name="b_member_nickname", length = 100, nullable = false,unique=true)
    private String bMemberNickname;

    @Column(name="b_member_name", length = 100, nullable = false)
    private String bMemberName;

    @Column( name="b_member_birth",nullable = false)
    private LocalDate bMemberBirth;

    @Column( name="b_member_phone_number",length = 100, nullable = false)
    private String bMemberPhoneNumber;

    @Column(name = "can_post", nullable = false, columnDefinition = "TINYINT DEFAULT 0")// 후원글 작성 가능 1, 불가능 0 -> 증명서 인증되어야 작성가능
    private boolean canPost;

    @Column(name="b_member_address", length = 100, nullable = false)
    private String bMemberAddress;

    @Column(name="b_member_certificate",nullable = false) //증명서 이미지
    private String bMemberCertificate;


    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "ENUM('ROLE_USER', 'ROLE_ADMIN') DEFAULT 'ROLE_USER'")
    private Role role;

    private String token;

    @Column(name = "is_verified", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private boolean isVerified;//초기 0, 증명 완료 1

    @Column(name="profile_path", nullable = false)
    private String profilePath;

    @OneToMany(mappedBy = "writer", cascade = {CascadeType.REMOVE})
    List<Post> posts;

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.bMemberPw = passwordEncoder.encode(bMemberPw);
    }

}
