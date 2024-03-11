package com.my.batch.service;

import com.my.batch.common.utils.ExchangeUtils;
import com.my.batch.constant.ResultCode;
import com.my.batch.domain.Exchange;
import com.my.batch.dto.exchange.response.ExchangeListResponseDto;
import com.my.batch.dto.exchange.response.ExchangeWebApiResponseDto;
import com.my.batch.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        List<Exchange> exchanges = exchangeRepository.findAll();
        return exchanges.size() > 0 ?
                ExchangeListResponseDto.builder()
                        .code(ResultCode.SUCCESS.getCode())
                        .exchangeDtoList(
                                exchanges.stream().map((it) -> (
                                        ExchangeListResponseDto.ExchangeDto.builder()
                                                .id(it.getId())
                                                .name(it.getName())
                                                .unit(it.getUnit())
                                                .dealBasR(it.getDealBasR())
                                                .exchangeRate(it.getExchangeRate())
                                                .ttb(it.getTtb())
                                                .tts(it.getTts())
                                                .build()
                                )).collect(Collectors.toList())
                        )
                        .build()
                : ExchangeListResponseDto.builder()
                .code(ResultCode.NO_CONTENT.getCode())
                .build();
    }

    public void saveExchangeList(List<ExchangeWebApiResponseDto> exchangeWebApiResponseDtoList){
        for(ExchangeWebApiResponseDto dto: exchangeWebApiResponseDtoList){
            Double dealBasR = Double.valueOf(dto.getDeal_bas_r());
            exchangeRepository.save(
                    Exchange.builder()
                            .name(dto.getCur_nm().split(" ")[0])
                            .krUnit(dto.getCur_nm().split(" ")[1])
                            .unit(dto.getCur_unit())
                            .dealBasR(dealBasR)
                            .exchangeRate(1000/dealBasR)
                            .ttb(Double.valueOf(dto.getTtb()))
                            .tts(Double.valueOf(dto.getTts()))
                            .build()
            );
        }
    }

    public void forceSaveExchangeList(String date){

    }


}
