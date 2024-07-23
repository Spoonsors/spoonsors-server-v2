package com.spoonsors.spoonsorsserver.entity.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class FcmRequestDto {

    private String targetToken;
    private String targetId;
    private Long postOrSponId;
    private String state; //매칭, 리뷰 등록(수혜자 -> 후원자, 글에 리뷰가 등록된 경우), 리뷰 요구(배송이 완료된 경우_백에서 처리)

}
