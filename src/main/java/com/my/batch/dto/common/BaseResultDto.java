package com.my.batch.dto.common;

import com.my.batch.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BaseResultDto {

    ResultCode code;

}
