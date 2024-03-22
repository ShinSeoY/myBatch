package com.my.batch.dto.coolsms.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendSMSRequestDto {

    String name;
    String exchangeRate;
    String unit;
    String krUnit;

}