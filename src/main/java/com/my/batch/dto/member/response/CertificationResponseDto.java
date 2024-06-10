package com.my.batch.dto.member.response;

import com.my.batch.dto.common.BaseResultDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class CertificationResponseDto extends BaseResultDto {

    private Boolean isValid;

}
