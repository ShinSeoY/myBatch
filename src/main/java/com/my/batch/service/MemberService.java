package com.my.batch.service;

import com.my.batch.domain.Member;
import com.my.batch.dto.signup.SignupRequestDto;
import com.my.batch.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void saveMember(SignupRequestDto signupRequestDto){
        memberRepository.save(
                Member.builder()
                        .userId(signupRequestDto.getUserId())
                        .name(signupRequestDto.getName())
                        .password(signupRequestDto.getPassword())
                        .regNo(signupRequestDto.getRegNo())
                        .build()
        );
    }

}
