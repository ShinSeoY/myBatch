package com.my.batch.dto.member;

import com.my.batch.dto.common.BaseResultDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LoginResponseDto extends BaseResultDto {

    String jwtToken;

}
