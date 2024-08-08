package com.spoonsors.spoonsorsserver.entity.spon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SponTidDto {
    String memberId;
    List<Long> spons;
    public String tid;
}
