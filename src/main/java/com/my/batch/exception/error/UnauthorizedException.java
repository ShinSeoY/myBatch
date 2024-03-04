package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnauthorizedException extends RuntimeException {

  private final ResultCode resultCode;

  public UnauthorizedException(){
    super(ResultCode.UNAUTHORIZED.getCode());
    this.resultCode = ResultCode.UNAUTHORIZED;
  }
}
