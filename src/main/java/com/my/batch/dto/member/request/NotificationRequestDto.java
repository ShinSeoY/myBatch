package com.my.batch.dto.member.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class NotificationRequestDto {
    Double goalExchangeRate;
    String calcType; // gte || lte
    List<String> enabledNotificatonList;
}
