package com.my.batch.service;

import com.my.batch.common.utils.ExchangeUtils;
import com.my.batch.constant.ResultCode;
import com.my.batch.domain.Exchange;
import com.my.batch.dto.exchange.response.ExchangeListResponseDto;
import com.my.batch.dto.exchange.response.ExchangeWebApiResponseDto;
import com.my.batch.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;
    private final ExchangeUtils exchangeUtils;

    public ExchangeListResponseDto findExchangeList() {
        List<Exchange> exchanges = exchangeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return exchanges.size() > 0 ?
                ExchangeListResponseDto.builder()
                        .code(ResultCode.SUCCESS.getCode())
                        .exchangeDtoList(
                                exchanges.stream().map((it) -> (
                                        ExchangeListResponseDto.ExchangeDto.builder()
                                                .name(it.getName())
                                                .unit(it.getUnit())
                                                .krUnit(it.getKrUnit())
                                                .dealBasR(it.getDealBasR())
                                                .exchangeRate(it.getExchangeRate())
                                                .ttb(it.getTtb())
                                                .tts(it.getTts())
                                                .updatedAt(it.getUpdatedAt())
                                                .build()
                                )).collect(Collectors.toList())
                        )
                        .build()
                : ExchangeListResponseDto.builder()
                .code(ResultCode.NO_CONTENT.getCode())
                .build();
    }

    public void saveExchangeList(List<ExchangeWebApiResponseDto> exchangeWebApiResponseDtoList) {
        for (ExchangeWebApiResponseDto dto : exchangeWebApiResponseDtoList) {
            exchangeRepository.save(ExchangeWebApiResponseDto.toExchange(dto));
        }
    }

    public void forceSaveExchangeList(String date) {

    }


}
