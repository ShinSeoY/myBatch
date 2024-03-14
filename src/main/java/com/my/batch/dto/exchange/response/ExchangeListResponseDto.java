package com.my.batch.dto.exchange.response;

import com.my.batch.dto.common.BaseResultDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ExchangeListResponseDto extends BaseResultDto {

    List<ExchangeDto> exchangeDtoList;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExchangeDto {

        Integer id;
        String name;
        String unit;
        String krUnit;
        Double dealBasR;
        Double exchangeRate;
        Double ttb;
        Double tts;
        LocalDateTime updatedAt;
    }

}