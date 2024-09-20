package com.my.batch.service;

import com.my.batch.common.security.AuthenticationTokenProvider;
import com.my.batch.common.utils.CryptoDbUtil;
import com.my.batch.constant.*;
import com.my.batch.domain.*;
import com.my.batch.dto.common.BaseResultDto;
import com.my.batch.dto.common.msg.SmsEvent;
import com.my.batch.dto.member.request.CertificationRequestDto;
import com.my.batch.dto.member.request.MemberRequestDto;
import com.my.batch.dto.member.request.NotificationRequestDto;
import com.my.batch.dto.member.response.*;
import com.my.batch.exception.error.NotFoundUserException;
import com.my.batch.exception.error.SendMsgFailErrorException;
import com.my.batch.exception.error.UncaughtException;
import com.my.batch.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberExchangeRepository memberExchangeRepository;
    private final ExchangeRepository exchangeRepository;
    private final NotificationRepository notificationRepository;
    private final MessageRepository messageRepository;

    private final CryptoDbUtil cryptoDbUtil;
    private final AuthenticationTokenProvider authenticationTokenProvider;
    private final ApplicationEventPublisher publisher;

    private final String EMAIL = "email";
    private final String SMS = "sms";

    public BaseResultDto sendCertificationMsg(String phone) {
        try {
            Message message = buildCertificationMessage(phone);
            publisher.publishEvent(new SmsEvent(cryptoDbUtil, null, message, message.getPhone()));
            return BaseResultDto.builder()
                    .code(ResultCode.SUCCESS.getCode())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SendMsgFailErrorException();
        }
    }

    private Message buildCertificationMessage(String phone) throws Exception {
        return Message.builder()
                .phone(cryptoDbUtil.encrypt(phone))
                .content("인증번호는 [" + getRandomNum() + "] 입니다")
                .msgType(MsgType.SMS)
                .msgTemplateType(MsgTemplateType.CERTIFICATION)
                .sendStatus(SendType.SENDING)
                .build();
    }

    private String getRandomNum() {
        Integer length = 6;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public CertificationResponseDto checkCertificationMsg(CertificationRequestDto certificationRequestDto) {
        try {
            Page<Message> page = messageRepository.findByCertification(cryptoDbUtil.encrypt(certificationRequestDto.getPhone()), certificationRequestDto.getCertificationMsg(), PageRequest.of(0, 1));
            if (page.getTotalPages() > 0) {
                return CertificationResponseDto.builder()
                        .code(ResultCode.SUCCESS.getCode())
                        .isValid(true)
                        .build();
            } else {
                return CertificationResponseDto.builder()
                        .code(ResultCode.SUCCESS.getCode())
                        .isValid(false)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UncaughtException();
        }

    }

    public void saveMember(MemberRequestDto memberRequestDto) throws Exception {
        memberRepository.save(
                Member.builder()
                        .email(memberRequestDto.getEmail())
                        .phone(cryptoDbUtil.encrypt(memberRequestDto.getPhone()))
                        .build()
        );
    }

    public LoginResponseDto login(MemberRequestDto memberRequestDto) {
        try {
            Optional<Member> found = memberRepository.findByEmail(memberRequestDto.getEmail());
            Member member = found.orElseThrow(() -> new NotFoundUserException());
            if (isMatch(memberRequestDto.getPhone(), member.getPhone())) {
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

    private boolean isMatch(String incomeText, String originText) {
        try {
            return incomeText.equals(cryptoDbUtil.decrypt(originText));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public CheckEmailResponseDto checkEmail(String email) {
        Integer count = memberRepository.countByEmail(email);
        return CheckEmailResponseDto.builder()
                .code(ResultCode.SUCCESS.getCode())
                .isDuplicate(email == null || count == null || count > 0)
                .build();
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

    public NotificationResponseDto findNotification(Member member) {
        if (member.getEmail().equals("test")) {
            return NotificationResponseDto.builder()
                    .code(ResultCode.SUCCESS.getCode())
                    .result(
                            NotificationResponseDto.NotificationResponse.builder()
                                    .isTestAccount(true)
                                    .build()
                    ).build();
        }
        Notification notification = notificationRepository.findByMemberId(member.getId());
        if (notification != null) {
            return NotificationResponseDto.builder()
                    .code(ResultCode.SUCCESS.getCode())
                    .result(
                            NotificationResponseDto.NotificationResponse.builder()
                                    .isTestAccount(false)
                                    .unit(notification.getExchange().getUnit())
                                    .goalExchangeRate(notification.getGoalExchangeRate())
                                    .calcType(notification.getCalcType())
                                    .emailEnabled(notification.isEmailEnabled())
                                    .smsEnabled(notification.isSmsEnabled())
                                    .build()
                    ).build();
        }
        return NotificationResponseDto.builder()
                .code(ResultCode.NO_CONTENT.getCode())
                .build();
    }

    @Transactional
    public BaseResultDto enableNotification(Member member, NotificationRequestDto notificationRequestDto) {
        Notification prevNotification = notificationRepository.findByMemberId(member.getId());

        // 기존 알림 삭제
        if (prevNotification != null) {
            notificationRepository.delete(prevNotification);
        }

        if (notificationRequestDto != null && !notificationRequestDto.getEnabledNotificatonList().isEmpty()) {
            Exchange exchange = exchangeRepository.findById(notificationRequestDto.getUnit()).orElseThrow();

            boolean isEnabledEmail = notificationRequestDto.getEnabledNotificatonList().contains(EMAIL);
            boolean isEnabledSms = notificationRequestDto.getEnabledNotificatonList().contains(SMS);

            Notification notification = Notification.builder()
                    .member(member)
                    .exchange(exchange)
                    .smsEnabled(isEnabledSms)
                    .emailEnabled(isEnabledEmail)
                    .calcType(CalcType.of(notificationRequestDto.getCalcType()))
                    .goalExchangeRate(notificationRequestDto.getGoalExchangeRate())
                    .isEnabled(true)
                    .build();
            notificationRepository.save(notification);
        }
        return BaseResultDto.builder()
                .code(ResultCode.SUCCESS.getCode())
                .build();
    }

}
