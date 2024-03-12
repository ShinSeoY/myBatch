package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UndefinedRequestApiException extends RuntimeException {

    private final ResultCode resultCode;

    public UndefinedRequestApiException() {
        super(ResultCode.UNDEFINED_REQUEST_API.getCode());
        this.resultCode = ResultCode.UNDEFINED_REQUEST_API;
    }
}
