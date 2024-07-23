package com.spoonsors.spoonsorsserver.entity.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDto {
    private String member_id;
    private String member_nickname;
    private String member_name;
    private String member_phoneNumber;
    private String member_address;
    private String member_profilePath;
    private String token;

}
