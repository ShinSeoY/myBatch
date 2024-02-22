package com.my.batch.controller;

import com.my.batch.dto.signup.SignupRequestDto;
import com.my.batch.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class BatchController {

    private final MemberService memberService;

    @PostMapping("")
    public ResponseEntity saveMember(@RequestBody SignupRequestDto signupRequestDto) {
        memberService.saveMember(signupRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("")
//    public ResponseEntity getMemberList(@LoginUser Member member) {
//        return new ResponseEntity<>(memberService.getRefund(member.getUserId()), HttpStatus.OK);
//    }
}
