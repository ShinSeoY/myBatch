package com.my.batch.dto.notification.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidNotificationResponseDto {

    private Integer notificationId;
    private String content;

}