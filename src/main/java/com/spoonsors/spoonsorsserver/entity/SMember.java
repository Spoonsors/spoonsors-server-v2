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
    private String sMember_id;

    @JsonIgnore
    @Column(length = 100, nullable = false)
    private String sMember_pwd;

    @Column(length = 100, nullable = false, unique=true)
    private String sMember_nickname;

    @Column(length = 100, nullable = false)
    private String sMember_name;

    @JsonIgnore
    @Column(length = 100, nullable = false)
    private String sMember_phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "sMember")
    private List<Spon> spons = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    private String token;

    @Column(nullable = false)
    private String profile_path;

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.sMember_pwd = passwordEncoder.encode(sMember_pwd);
    }
}
