package com.my.batch.dto.exchange.request;

import com.my.batch.dto.common.PageBaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ListSearchRequestDto {

    private String keyword;
    private PageBaseDto pageBaseDto;

}
