package com.my.batch.dto.exchange.response;

import com.my.batch.domain.Exchange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

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

    static List<String> specificNames = Arrays.asList("IDR(100)", "JPY(100)");

    static public Exchange toExchange(ExchangeWebApiResponseDto dto) {
        Double dealBasR = Double.valueOf(dto.getDeal_bas_r().replaceAll(",", ""));
        return Exchange.builder()
                .name(validName(dto.getCur_nm(), dto.getCur_unit()))
                .unit(validUnit(dto.getCur_unit()))
                .krUnit(validKrUnit(dto.getCur_nm()))
                .dealBasR(dealBasR)
                .build();
    }

    private static String validName(String curNm, String curUnit) {
        if (curUnit.equals("CNH")) {
            return "중국";
        } else {
            return curNm.split(" ")[0];
        }
    }

    private static String validUnit(String curUnit) {
        if (specificNames.contains(curUnit)) {
            return curUnit.replaceAll("\\(100\\)", "");
        } else {
            return curUnit;
        }
    }

    private static String validKrUnit(String curNm) {
        if (curNm.split(" ").length > 1) {
            return curNm.split(" ")[1];
        } else {
            return curNm.split(" ")[0];
        }
    }

    private static Double calcExchangeRage(Double dealBasR, String curUnit) {
        if (specificNames.contains(curUnit)) {
            return (int) ((1000 / dealBasR * 100) * 100) / 100.0;
        }
        return (int) ((1000 / dealBasR) * 100) / 100.0;
    }

}