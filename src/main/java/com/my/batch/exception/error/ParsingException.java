package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParsingException extends RuntimeException {

    private final ResultCode resultCode;

    public ParsingException() {
        super(ResultCode.PARSE_ERROR.getCode());
        this.resultCode = ResultCode.PARSE_ERROR;
    }
}
