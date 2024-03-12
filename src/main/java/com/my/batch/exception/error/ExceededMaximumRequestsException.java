package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceededMaximumRequestsException extends RuntimeException {

    private final ResultCode resultCode;

    public ExceededMaximumRequestsException() {
        super(ResultCode.EXCEEDED_MAXIMUM_REQUESTS.getCode());
        this.resultCode = ResultCode.EXCEEDED_MAXIMUM_REQUESTS;
    }
}
