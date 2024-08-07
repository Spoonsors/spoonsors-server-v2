package com.spoonsors.spoonsorsserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
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
public class SMember extends Member {

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "ENUM('ROLE_SMEMBER', 'ROLE_BMEMBER','ROLE_ADMIN') DEFAULT 'ROLE_SMEMBER'")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "sMember")
    private final List<Spon> spons;
}
