package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidValueException extends RuntimeException {

  private final ResultCode resultCode;

  public InvalidValueException(){
    super(ResultCode.INVALID.getCode());
    this.resultCode = ResultCode.INVALID;
  }
}
