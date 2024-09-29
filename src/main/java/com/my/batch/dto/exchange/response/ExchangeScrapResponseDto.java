package com.my.batch.dto.exchange.response;

import com.my.batch.domain.Exchange;
import lombok.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class ExchangeScrapResponseDto implements Serializable {

    private String unit;
    private String name;
    private String kr_unit;
    private double deal_basr;

    static public Exchange toExchange(ExchangeScrapResponseDto dto) {
        Exchange exchange = Exchange.builder()
                .name(validName(dto.name))
                .unit(dto.unit)
                .krUnit(dto.kr_unit)
                .dealBasR(dto.deal_basr)
                .build();

        List<String> hundredUnit = Arrays.asList("LKR", "JPY");
        List<String> thousandUnit = Arrays.asList("VND", "IDR", "CLP", "KHR", "HUF", "MNT", "MMK", "UZS", "TZS", "PKR", "COP");

        if (hundredUnit.contains(dto.getUnit())) {
            exchange.setDisplayUnit(100);
        }
        if (thousandUnit.contains(dto.getUnit())) {
            exchange.setDisplayUnit(1000);
        }
        return exchange;
    }

    private static String validName(String name) {
        return name.split(" ")[0];
    }

}