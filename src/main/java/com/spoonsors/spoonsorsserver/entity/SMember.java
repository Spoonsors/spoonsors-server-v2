package com.spoonsors.spoonsorsserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name="smember",
        uniqueConstraints={
                @UniqueConstraint(
                        name="phoneAndName",
                        columnNames={"sMember_name", "sMember_phoneNumber"}
                )
        }
)
public class SMember extends BaseTime {
    @Id
    @Column(length = 100, nullable = false)
    private String sMemberId;

    @Column(name = "sMember_pw", nullable = false, length = 100)
    private String sMemberPw;

    @Column(name = "sMember_nickname", nullable = false, length = 100, unique = true)
    private String sMemberNickname;

    @Column(name = "sMember_name", nullable = false, length = 100)
    private String sMemberName;

    @Column(name = "sMember_phoneNumber", nullable = false, length = 100)
    private String sMemberPhoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "sMember")
    private List<Spon> spons = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "ENUM('ROLE_USER', 'ROLE_ADMIN') DEFAULT 'ROLE_USER'")
    private Role role;

    @Column(name = "token", length = 255)
    private String token;


    @Column(name = "profile_path", nullable = false, length = 255)
    private String profilePath;

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.sMemberPw = passwordEncoder.encode(sMemberPw);
    }
}
