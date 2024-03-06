package com.my.batch.controller;

import com.my.batch.annotation.LoginUser;
import com.my.batch.domain.Member;
import com.my.batch.dto.member.request.MemberRequestDto;
import com.my.batch.dto.member.response.LoginResponseDto;
import com.my.batch.dto.member.response.MemberFavListResponseDto;
import com.my.batch.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity saveMember(@RequestBody MemberRequestDto memberRequestDto) {
        memberService.saveMember(memberRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody MemberRequestDto memberRequestDto) {
        return new ResponseEntity<>(memberService.login(memberRequestDto), HttpStatus.OK);
    }

    @GetMapping("/fav")
    public ResponseEntity<MemberFavListResponseDto> login(@LoginUser Member member) {
        return new ResponseEntity<>(memberService.getMemberFavList(member), HttpStatus.OK);
    }
}
