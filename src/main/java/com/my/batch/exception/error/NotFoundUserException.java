package com.my.batch.exception.error;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotFoundUserException extends RuntimeException {

  private final ResultCode resultCode;

  public NotFoundUserException(){
    super(ResultCode.NOT_FOUND_USER.getCode());
    this.resultCode = ResultCode.NOT_FOUND_USER;
  }
}
