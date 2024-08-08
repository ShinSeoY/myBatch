package com.my.batch.dto.exchange.request;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class BatchStatusRequestDto {

    private String workflowId;
    private String workflowName;
    private String status;
    private long duration;
    private String startTime;
    private String endTime;
    private String failedStepName;
    private String errMsg;

}
