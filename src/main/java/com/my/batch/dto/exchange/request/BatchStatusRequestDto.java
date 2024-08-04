package com.my.batch.dto.exchange.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BatchStatusRequestDto {

    private String dagName;
    private String jobName;
    private String status;
    private long duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String failedStepName;
    private String errMsg;

}
