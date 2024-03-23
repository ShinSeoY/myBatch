package com.my.batch.dto.smtp.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendEmailRequestDto {

    private String address;
    private String title;
    private String content;

}