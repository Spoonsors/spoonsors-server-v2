package com.spoonsors.spoonsorsserver.entity.authorize;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import org.springframework.data.annotation.Id;

@RedisHash(value = "auth",timeToLive = 180) // 데이터 만료 시간 180초
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Auth {

    @Id
    private String phoneNum;
    private String code;
}
