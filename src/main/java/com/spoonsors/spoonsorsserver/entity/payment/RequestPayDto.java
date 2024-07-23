package com.spoonsors.spoonsorsserver.entity.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RequestPayDto {
    private String tid;
    private String next_redirect_app_url;
    private Date created_at;
    private String SMemberId;
}
