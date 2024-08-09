package com.spoonsors.spoonsorsserver.controller.member;

import com.spoonsors.spoonsorsserver.entity.Post;
import com.spoonsors.spoonsorsserver.entity.bMember.ViewPostDto;
import com.spoonsors.spoonsorsserver.entity.bMember.WritePostDto;
import com.spoonsors.spoonsorsserver.service.member.PostService;
import com.spoonsors.spoonsorsserver.service.spon.SponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final SponService sponService;

    @Operation(summary = "후원 요청 글 작성", description = "후원 요청 글을 작성합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터")
    })
    @PostMapping
    public String writePost(@RequestBody WritePostDto writePostDto) {
        String memberId = "user123"; //TODO:로그인 정보 가져오기
        Post post=postService.writePost(memberId, writePostDto);
        sponService.addSpon(writePostDto.getItem_list(), post.getPostId());
        return "["+post.getPostId()+"]"+ "\""+post.getPostTitle()+"\""+ " 작성 완료";
    }

    @Operation(summary = "글 삭제", description = "후원 요청 글을 삭제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "글을 찾을 수 없음")
    })
    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Long postId) throws IOException {
        postService.delete(postId);
        return postId + "번 post 삭제 완료";
    }

    @Operation(summary = "모든 글 조회", description = "모든 후원 글 목록을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "글 목록 조회 성공")
    })
    @GetMapping("/posts")
    public List<Post> viewAllPosts(){
        return postService.viewAllPosts();
    }

    @Operation(summary = "단일 글 조회", description = "ID를 통해 후원 글을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "글을 찾을 수 없음")
    })
    @GetMapping("/posts/{postId}")
    public ViewPostDto viewPost(@PathVariable Long postId){
        return postService.viewPost(postId);
    }

    @Operation(summary = "회원별 글 목록 조회", description = "특정 회원이 작성한 모든 글을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원의 글 목록 조회 성공")
    })
    @GetMapping("bMember/posts")
    public List<Post> viewMyPosts(){
        String memberId = "user123"; //TODO:로그인 정보 가져오기
        return postService.viewMyPosts(memberId);
    }

    @Operation(summary = "글 상태 변경", description = "글의 상태를 변경합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "글 상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "글을 찾을 수 없음")
    })
    @PostMapping("/bMember/posts/{postId}/state")
    public String changePostState(@PathVariable Long postId) throws IOException {
        return postService.changePostState(postId);
    }
}
