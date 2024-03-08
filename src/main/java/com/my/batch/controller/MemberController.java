package com.my.batch.controller;

import com.my.batch.annotation.LoginUser;
import com.my.batch.domain.Exchange;
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
    public ResponseEntity<MemberFavListResponseDto> getMemberFavList(@LoginUser Member member) {
        return new ResponseEntity<>(memberService.getMemberFavList(member), HttpStatus.OK);
    }

    @PostMapping("/fav")
    public ResponseEntity<BaseResultDto> saveMemberFav(@LoginUser Member member, @RequestBody List<Integer> exchangeIdList) {
        return new ResponseEntity<>(memberService.saveMemberFav(member, exchangeIdList), HttpStatus.OK);
    }

    @DeleteMapping("/fav/{exchangeId}")
    public ResponseEntity<BaseResultDto> deleteMemberFav(@LoginUser Member member, @PathVariable Integer exchangeId) {
        return new ResponseEntity<>(memberService.deleteMemberFav(member, exchangeId), HttpStatus.OK);
    }
}
