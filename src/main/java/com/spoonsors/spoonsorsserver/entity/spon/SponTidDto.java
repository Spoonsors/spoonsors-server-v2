package com.spoonsors.spoonsorsserver.entity.spon;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SponTidDto {
    String memberId;
    List<Long> spons;
    public String tid;
}
