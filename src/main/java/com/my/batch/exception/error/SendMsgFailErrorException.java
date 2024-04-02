package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendMsgFailErrorException extends RuntimeException {

    private final ResultCode resultCode;

    public SendMsgFailErrorException() {
        super(ResultCode.SEND_MSG_FAIL.getCode());
        this.resultCode = ResultCode.SEND_MSG_FAIL;
    }
}
