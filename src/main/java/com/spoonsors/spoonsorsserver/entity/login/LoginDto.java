package com.spoonsors.spoonsorsserver.entity.login;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDto {
    private String id;
    private String nickname;
    private String name;
    private String phoneNumber;
    private String profilePath;
    private String address;
    private String token;

}
