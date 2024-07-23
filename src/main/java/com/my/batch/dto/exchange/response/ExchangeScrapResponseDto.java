package com.my.batch.dto.exchange.response;

import com.my.batch.domain.Exchange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeScrapResponseDto {

    private String unit;
    private String name;
    private String kr_unit;
    private double deal_basr;

    static public Exchange toExchange(ExchangeScrapResponseDto dto) {
        return Exchange.builder()
                .name(validName(dto.name))
                .unit(dto.unit)
                .krUnit(dto.kr_unit)
                .dealBasR(dto.deal_basr)
                .build();
    }

    private static String validName(String name) {
        return name.split(" ")[0];
    }

}