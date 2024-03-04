package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnauthorizedScrapException extends RuntimeException {

    private final ResultCode resultCode;

    public UnauthorizedScrapException() {
        super(ResultCode.UNAUTHORIZED_SCRAP.getCode());
        this.resultCode = ResultCode.UNAUTHORIZED_SCRAP;
    }
}
