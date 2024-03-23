package com.my.batch.controller;

import com.my.batch.annotation.LoginUser;
import com.my.batch.common.security.AuthenticationTokenProvider;
import com.my.batch.domain.Member;
import com.my.batch.dto.common.BaseResultDto;
import com.my.batch.dto.member.request.MemberRequestDto;
import com.my.batch.dto.member.response.LoginResponseDto;
import com.my.batch.dto.member.response.MemberFavListResponseDto;
import com.my.batch.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationTokenProvider authenticationTokenProvider;

    /**
     * @param memberRequestDto
     * @apiNote 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity saveMember(@RequestBody MemberRequestDto memberRequestDto) {
        memberService.saveMember(memberRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param memberRequestDto
     * @apiNote 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody MemberRequestDto memberRequestDto) {
        return new ResponseEntity<>(memberService.login(memberRequestDto), HttpStatus.OK);
    }

    /**
     * @param token
     * @apiNote 토큰 검증
     */
    @PostMapping("/verify-token")
    public ResponseEntity<Boolean> login(@RequestBody String token) {
        return new ResponseEntity<>(authenticationTokenProvider.verifyToken(token), HttpStatus.OK);
    }

    /**
     * @param member
     * @apiNote 즐겨찾기 리스트 조회
     */
    @GetMapping("/fav")
    public ResponseEntity<MemberFavListResponseDto> getMemberFavList(@LoginUser Member member) {
        return new ResponseEntity<>(memberService.getMemberFavList(member), HttpStatus.OK);
    }

    /**
     * @param member
     * @param exchangeUnitList
     * @apiNote 즐겨찾기 추가
     */
    @PostMapping("/fav")
    public ResponseEntity<BaseResultDto> saveMemberFav(@LoginUser Member member, @RequestBody List<String> exchangeUnitList) {
        return new ResponseEntity<>(memberService.saveMemberFav(member, exchangeUnitList), HttpStatus.OK);
    }

    /**
     * @param member
     * @param exchangeUnit
     * @apiNote 즐겨찾기 삭제
     */
    @DeleteMapping("/fav/{exchangeUnit}")
    public ResponseEntity<BaseResultDto> deleteMemberFav(@LoginUser Member member, @PathVariable String exchangeUnit) {
        return new ResponseEntity<>(memberService.deleteMemberFav(member, exchangeUnit), HttpStatus.OK);
    }

    /**
     * @param member
     * @param enabledNotificatonList
     * @apiNote 알림 설정
     */
    @PostMapping("/notification")
    public ResponseEntity<BaseResultDto> enableNotification(@LoginUser Member member, @RequestBody List<String> enabledNotificatonList) {
        return new ResponseEntity<>(memberService.enableNotification(member, enabledNotificatonList), HttpStatus.OK);
    }
}
