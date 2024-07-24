package com.spoonsors.spoonsorsserver.controller.member;

import com.spoonsors.spoonsorsserver.entity.Post;
import com.spoonsors.spoonsorsserver.entity.bMember.ViewPostDto;
import com.spoonsors.spoonsorsserver.entity.bMember.WritePostDto;
import com.spoonsors.spoonsorsserver.service.member.PostService;
import com.spoonsors.spoonsorsserver.service.spon.SponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final SponService sponService;

    //후원 글 작성
    @PostMapping("/bMember/post/{bMemberId}")
    public String writePost(@PathVariable String bMemberId, @RequestBody WritePostDto writePostDto) throws IOException {
        Post post=postService.writePost(bMemberId, writePostDto);
        sponService.addSpon(writePostDto.getItem_list(), post.getPostId());
        return "["+post.getPostId()+"]"+ "\""+post.getPostTitle()+"\""+ " 작성 완료";
    }

    //후원 글 삭제
    @DeleteMapping("/bMember/deletePost/{postId}")
    public String deletePost(@PathVariable Long postId) throws IOException {
        postService.delete(postId);
        return postId + "번 post 삭제 완료";
    }
    //후원 글 목록 확인
    @GetMapping("/viewPosting")
    public List<Post> viewAllPosts(){
        List<Post> posts=postService.viewAllPosts();
        return posts;
    }

    //단일 글 확인
    @GetMapping("/viewPosting/{postId}")
    public ViewPostDto viewPost(@PathVariable Long postId){
        ViewPostDto post=postService.viewPost(postId);
        return post;
    }

    //내가 작성한 글 보기
    @GetMapping("/viewMyPosting/{bMemberId}")
    public List<Post> viewMyPosts(@PathVariable String bMemberId){
        List<Post> posts=postService.viewMyPosts(bMemberId);
        return posts;
    }

    //글 상태 변경
    @PostMapping("/bMember/changePostState/{postId}")
    public String changePostState(@PathVariable Long postId) throws IOException {

        return postService.changePostState(postId);
    }
}
