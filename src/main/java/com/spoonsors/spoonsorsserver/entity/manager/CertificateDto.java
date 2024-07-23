package com.spoonsors.spoonsorsserver.entity.manager;

import com.spoonsors.spoonsorsserver.entity.BMember;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CertificateDto {
    private String bMember_id;

    private String bMember_name;

    private String bMember_birth;

    private String bMember_phoneNumber;

    private String bMember_address;

    private String bMember_certificate;

    private int is_verified;
}
