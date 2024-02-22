package com.my.batch.dto.signup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SignupRequestDto {

    String userId;
    String password;
    String name;
    String regNo;

}
