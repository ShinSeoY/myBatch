package com.my.batch.exception;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

  private LocalDateTime timestamp;

  private String path;

  private String code;

  private String message;

  public ErrorResponse(final ResultCode code){
    this.timestamp = LocalDateTime.now();
    this.message = code.getMessage();
    this.code = code.getCode();
  }

  public ErrorResponse(final ResultCode code, final String path){
    this.timestamp = LocalDateTime.now();
    this.path = path;
    this.message = code.getMessage();
    this.code = code.getCode();
  }

  public static ErrorResponse of(final ResultCode code, final String path){
    return new ErrorResponse(code, path);
  }


}
