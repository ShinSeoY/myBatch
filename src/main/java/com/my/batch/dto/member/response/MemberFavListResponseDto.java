package com.my.batch.dto.member.response;

import com.my.batch.dto.common.BaseResultDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MemberFavListResponseDto extends BaseResultDto {

    List<MemberFavDto> memberFavDtoList;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberFavDto {

        Integer id;
        String name;
        String unit;
        Double dealBasR;
        Double exchangeRate;

    }

}