package com.spoonsors.spoonsorsserver.controller.member;

import com.spoonsors.spoonsorsserver.entity.login.LoginDto;
import com.spoonsors.spoonsorsserver.entity.sMember.SMemberSignUpDto;
import com.spoonsors.spoonsorsserver.entity.sMember.TokenUpdateReqDto;
import com.spoonsors.spoonsorsserver.entity.spon.SponDto;
import com.spoonsors.spoonsorsserver.service.member.SMemberService;
import com.spoonsors.spoonsorsserver.service.spon.SponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sMembers")
@RequiredArgsConstructor
public class SMemberController {
    private final SMemberService sMemberService;
    private final SponService sponService;

    @Operation(summary = "회원 가입", description = "새로운 후원자를 가입시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<String> join(@Valid @RequestBody SMemberSignUpDto dto) throws Exception {
        String id = sMemberService.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id + " 회원가입 완료");
    }

    @Operation(summary = "로그인", description = "후원자 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestBody Map<String, String> member) {
        LoginDto loginDto = sMemberService.login(member);
        return ResponseEntity.ok(loginDto);
    }

    @Operation(summary = "토큰 갱신", description = "후원자의 토큰을 갱신합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 갱신 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/token")
    public ResponseEntity<String> putToken(@RequestBody TokenUpdateReqDto token) {
        sMemberService.putToken(token);
        return ResponseEntity.ok("완료");
    }

    @Operation(summary = "후원 내역 조회", description = "후원자가 후원한 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "후원 내역 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원이 존재하지 않거나 후원 내역이 없음")
    })
    @GetMapping("/{sMemberId}/spons")
    public ResponseEntity<List<SponDto>> getSponList(@PathVariable String sMemberId) {
        List<SponDto> sponList = sponService.getSponList(sMemberId);

        // 만약 sponList가 비어 있거나 존재하지 않는다면 404 상태 코드 반환
        if (sponList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(sponList);
    }
}