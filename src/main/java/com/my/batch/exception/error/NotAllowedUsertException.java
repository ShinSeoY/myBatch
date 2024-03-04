package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotAllowedUsertException extends RuntimeException {

    private final ResultCode resultCode;

    public NotAllowedUsertException() {
        super(ResultCode.NOT_ALLOWED_USER.getCode());
        this.resultCode = ResultCode.NOT_ALLOWED_USER;
    }
}
