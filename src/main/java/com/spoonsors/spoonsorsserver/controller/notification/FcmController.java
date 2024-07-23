package com.spoonsors.spoonsorsserver.controller.notification;

import com.spoonsors.spoonsorsserver.entity.notification.FcmRequestDto;
import com.spoonsors.spoonsorsserver.service.notificatin.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FCMService fcmService;

    @PostMapping("/fcm/post")
    public Long pushMessage(@RequestBody FcmRequestDto requestDTO) throws IOException {

        Long postId = fcmService.sendMessageTo(
                requestDTO.getTargetToken(),
                requestDTO.getTargetId(),
                requestDTO.getPostOrSponId(),
                requestDTO.getState());

        return postId;
    }
}
