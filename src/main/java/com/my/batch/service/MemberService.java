package com.my.batch.service;

import com.my.batch.constant.ResultCode;
import com.my.batch.domain.Member;
import com.my.batch.domain.MemberExchange;
import com.my.batch.dto.member.request.MemberRequestDto;
import com.my.batch.dto.member.response.LoginResponseDto;
import com.my.batch.dto.member.response.MemberFavListResponseDto;
import com.my.batch.exception.error.NotFoundUserException;
import com.my.batch.repository.MemberExchangeRepository;
import com.my.batch.repository.MemberRepository;
import com.my.batch.security.AuthenticationTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberExchangeRepository memberExchangeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationTokenProvider authenticationTokenProvider;

    public void saveMember(MemberRequestDto memberRequestDto) {
        memberRepository.save(
                Member.builder()
                        .email(memberRequestDto.getEmail())
                        .phone(bCryptPasswordEncoder.encode(memberRequestDto.getPhone()))
                        .build()
        );
    }

    public LoginResponseDto login(MemberRequestDto memberRequestDto) {
        try {
            Optional<Member> found = memberRepository.findByEmail(memberRequestDto.getEmail());
            Member member = found.orElseThrow(() -> new NotFoundUserException());
            if (bCryptPasswordEncoder.matches(memberRequestDto.getPhone(), member.getPhone())) {
                return LoginResponseDto.builder()
                        .jwtToken(authenticationTokenProvider.generateJwtToken(member))
                        .code(ResultCode.SUCCESS.getCode())
                        .build();
            } else {
                throw new NotFoundUserException();
            }
        } catch (NotFoundUserException e) {
            return LoginResponseDto.builder()
                    .code(ResultCode.NOT_FOUND_USER.getCode())
                    .build();
        }
    }

    public MemberFavListResponseDto getMemberFavList(Member member) {
        List<MemberExchange> member2 = memberExchangeRepository.findMemberExchanges(member.getEmail());
        return MemberFavListResponseDto.builder()
                .code(ResultCode.SUCCESS.getCode())
                .memberFavDtoList(
                        member2.stream().map((it) -> (
                                        MemberFavListResponseDto.MemberFavDto.builder()
                                                .name(it.getExchange().getName())
                                                .unit(it.getExchange().getUnit())
                                                .dealBasR(it.getExchange().getDealBasR())
                                                .exchangeRate(null)
                                                .build()
                                )
                        ).collect(Collectors.toList())
                )
                .build();
    }

}
