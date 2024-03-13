package com.my.batch.dto.exchange.response;

import com.my.batch.domain.Exchange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeWebApiResponseDto implements Serializable {

    private Integer result; // 결과
    private String cur_unit; // 통화코드
    private String cur_nm; // 국가/통화명
    private String ttb; // 전신환(송금) 받으실 때
    private String tts; // 전신환(송금) 보내실 때
    private String deal_bas_r; // 매매 기준율
    private String bkpr; // 장부가격
    private String yy_efee_r; // 년환가료율
    private String ten_dd_efee_r; // 10일환가료율
    private String kftc_bkpr; // 서울외국환중개 매매기준율
    private String kftc_deal_bas_r; // 서울외국환중개장부가격

    static public Exchange toExchange(ExchangeWebApiResponseDto dto) {
        Double dealBasR = Double.valueOf(dto.getDeal_bas_r().replaceAll(",", ""));
        return Exchange.builder()
                .name(dto.getCur_nm().split(" ")[0])
                .krUnit(validKrUnit(dto.getCur_nm()))
                .unit(dto.getCur_unit())
                .dealBasR(dealBasR)
                .exchangeRate(1000 / dealBasR)
                .ttb(Double.valueOf(dto.getTtb().replaceAll(",", "")))
                .tts(Double.valueOf(dto.getTts().replaceAll(",", "")))
                .build();
    }

    private static String validKrUnit(String curNm) {
        if (curNm.split(" ").length > 1) {
            return curNm.split(" ")[1];
        } else {
            return curNm.split(" ")[0];
        }
    }

}