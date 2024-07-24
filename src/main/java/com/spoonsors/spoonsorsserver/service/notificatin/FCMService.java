package com.spoonsors.spoonsorsserver.service.notificatin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.spoonsors.spoonsorsserver.customException.ApiException;
import com.spoonsors.spoonsorsserver.customException.ExceptionEnum;
import com.spoonsors.spoonsorsserver.entity.Post;
import com.spoonsors.spoonsorsserver.entity.SMember;
import com.spoonsors.spoonsorsserver.entity.Spon;
import com.spoonsors.spoonsorsserver.entity.notification.FCMDto;
import com.spoonsors.spoonsorsserver.repository.IPostRepository;
import com.spoonsors.spoonsorsserver.repository.ISMemberRepository;
import com.spoonsors.spoonsorsserver.repository.ISponRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FCMService {

    private final ISMemberRepository isMemberRepository;
    private final ISponRepository iSponRepository;
    private final IPostRepository iPostRepository;
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/spoonsors/messages:send";
    private final ObjectMapper objectMapper;

    public Long sendMessageTo(String targetToken, String targetId, Long postOrSponId, String state) throws IOException {
        String title="";
        String body="";
        Long postId= 0L;

        if(state.equals("매칭")){ //target token = 자립 준비 청년 토큰, targetId: 자립 준비 청년 아이디
            Optional<SMember> optionalSMember = isMemberRepository.findById(targetId);
            String targetNickname = optionalSMember.get().getMemberNickname();

            Optional<Spon> optionalSpon = iSponRepository.findById(postOrSponId);
            Spon spon = optionalSpon.get();

            postId=spon.getPost().getPostId();

            title="후원 매칭!";
            body= targetNickname + "님이 ["+spon.getPost().getPostTitle()+"]에 있는 "+spon.getIngredients().getIngredientsName()+"를 보냈어요! ";

        }else if(state.equals("리뷰 등록")){
            Optional<Post> optionalSpon = iPostRepository.findById(postOrSponId);
            Post post = optionalSpon.get();
            postId=post.getPostId();
            title="리뷰 등록!";
            body=post.getWriter().getMemberNickname()+"님이 후원 감사 리뷰를 보냈어요. 지금 확인해 보세요!";
        }else{
            throw new ApiException(ExceptionEnum.PUSH01);
        }

        String message = makeMessage(targetToken, title, body);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        return postId;
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FCMDto fcmDto = FCMDto.builder()
                .message(FCMDto.Message.builder()
                        .token(targetToken)
                        .notification(FCMDto.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmDto);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath="firebase/spoonsors-53f69990eebe.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}