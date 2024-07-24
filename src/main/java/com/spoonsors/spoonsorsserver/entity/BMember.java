package com.spoonsors.spoonsorsserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@SuperBuilder
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
public class BMember extends Member {
    @Column(name = "b_member_birth", nullable = false)
    private LocalDate bMemberBirth;

    @Column(name = "b_member_address", length = 100, nullable = false)
    private String bMemberAddress;

    @Column(name = "b_member_certificate", nullable = false)
    private String bMemberCertificate;

    @Column(name = "can_post", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private boolean canPost;

    @Column(name = "is_verified", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private boolean isVerified;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "ENUM('ROLE_SMEMBER', 'ROLE_BMEMBER','ROLE_ADMIN') DEFAULT 'ROLE_BMEMBER'")
    private Role role;

    @OneToMany(mappedBy = "writer", cascade = {CascadeType.REMOVE})
    private List<Post> posts;
}
