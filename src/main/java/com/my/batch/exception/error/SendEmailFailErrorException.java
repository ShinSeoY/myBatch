package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendEmailFailErrorException extends RuntimeException {

    private final ResultCode resultCode;

    public SendEmailFailErrorException() {
        super(ResultCode.SEND_EMAIL_FAIL.getCode());
        this.resultCode = ResultCode.SEND_EMAIL_FAIL;
    }
}
