package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidTokenException extends RuntimeException {

  private final ResultCode resultCode;

  public InvalidTokenException(){
    super(ResultCode.INVALID_TOKEN.getCode());
    this.resultCode = ResultCode.INVALID_TOKEN;
  }
}
