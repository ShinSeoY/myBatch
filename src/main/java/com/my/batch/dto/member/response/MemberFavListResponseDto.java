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

    List<MemberFav> memberFavList;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberFav {

        String name;
        String unit;
        Double dealBasR;
        Double exchangeRate;

    }

}