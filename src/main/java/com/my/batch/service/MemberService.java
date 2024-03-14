package com.my.batch.service;

import com.my.batch.common.security.AuthenticationTokenProvider;
import com.my.batch.constant.ResultCode;
import com.my.batch.domain.Exchange;
import com.my.batch.domain.Member;
import com.my.batch.domain.MemberExchange;
import com.my.batch.domain.MemberExchangeId;
import com.my.batch.dto.common.BaseResultDto;
import com.my.batch.dto.member.request.MemberRequestDto;
import com.my.batch.dto.member.response.LoginResponseDto;
import com.my.batch.dto.member.response.MemberFavListResponseDto;
import com.my.batch.exception.error.NotFoundUserException;
import com.my.batch.repository.ExchangeRepository;
import com.my.batch.repository.MemberExchangeRepository;
import com.my.batch.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberExchangeRepository memberExchangeRepository;
    private final ExchangeRepository exchangeRepository;
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
        List<MemberExchange> memberExchangeList = memberExchangeRepository.findMemberExchanges(member.getEmail(), Sort.by(Sort.Direction.DESC, "updatedAt"));
        return MemberFavListResponseDto.builder()
                .code(ResultCode.SUCCESS.getCode())
                .memberFavDtoList(
                        memberExchangeList.stream().map((it) -> (
                                        MemberFavListResponseDto.MemberFavDto.builder()
                                                .name(it.getExchange().getName())
                                                .unit(it.getExchange().getUnit())
                                                .krUnit(it.getExchange().getKrUnit())
                                                .dealBasR(it.getExchange().getDealBasR())
                                                .exchangeRate(it.getExchange().getExchangeRate())
                                                .build()
                                )
                        ).collect(Collectors.toList())
                )
                .build();
    }

    @Transactional
    public BaseResultDto saveMemberFav(Member member, List<String> exchangeUnitList) {
        for (String exchangeUnit : exchangeUnitList) {
            Exchange exchange = exchangeRepository.findById(exchangeUnit).orElseThrow();
            memberExchangeRepository.save(
                    MemberExchange.builder()
                            .id(
                                    MemberExchangeId.builder()
                                            .exchangeUnit(exchangeUnit)
                                            .memberId(member.getId())
                                            .build()
                            )
                            .exchange(exchange)
                            .member(member)
                            .build()
            );
        }
        return BaseResultDto.builder()
                .code(ResultCode.SUCCESS.getCode())
                .build();
    }

    @Transactional
    public BaseResultDto deleteMemberFav(Member member, String exchangeUnit) {
        memberExchangeRepository.deleteByMemberIdAndExchangeUnit(member.getId(), exchangeUnit);
        return BaseResultDto.builder()
                .code(ResultCode.SUCCESS.getCode())
                .build();
    }

}
