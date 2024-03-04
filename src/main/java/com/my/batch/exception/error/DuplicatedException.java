package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicatedException extends RuntimeException {

    private final ResultCode resultCode;

    public DuplicatedException() {
        super(ResultCode.DUPLICATED.getCode());
        this.resultCode = ResultCode.DUPLICATED;
    }
}
