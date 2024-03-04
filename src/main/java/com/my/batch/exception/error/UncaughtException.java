package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UncaughtException extends RuntimeException {

  private final ResultCode resultCode;

  public UncaughtException(){
    super(ResultCode.UNCAUGHT.getCode());
    this.resultCode = ResultCode.UNCAUGHT;
  }
}
