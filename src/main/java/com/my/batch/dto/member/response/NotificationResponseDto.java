package com.my.batch.dto.member.response;

import com.my.batch.constant.CalcType;
import com.my.batch.dto.common.BaseResultDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class NotificationResponseDto extends BaseResultDto {

    NotificationResponse result;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationResponse {
        private String unit;
        private Double goalExchangeRate;
        private CalcType calcType;
        private boolean smsEnabled;
        private boolean emailEnabled;
    }

}
