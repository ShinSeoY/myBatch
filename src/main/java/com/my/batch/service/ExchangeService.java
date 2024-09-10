package com.my.batch.service;

import com.my.batch.common.utils.ExchangeUtils;
import com.my.batch.constant.ResultCode;
import com.my.batch.domain.BatchStatus;
import com.my.batch.domain.Exchange;
import com.my.batch.dto.exchange.request.BatchStatusRequestDto;
import com.my.batch.dto.exchange.request.ListSearchRequestDto;
import com.my.batch.dto.exchange.response.ExchangeListResponseDto;
import com.my.batch.dto.exchange.response.ExchangeScrapResponseDto;
import com.my.batch.dto.exchange.response.ExchangeWebApiResponseDto;
import com.my.batch.repository.BatchStatusRepository;
import com.my.batch.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;
    private final ExchangeUtils exchangeUtils;
    private final BatchStatusRepository batchStatusRepository;

    public void saveBatchStatus(BatchStatusRequestDto batchStatusRequestDto) {
        OffsetDateTime startTime = OffsetDateTime.parse(batchStatusRequestDto.getStartTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime endTime = OffsetDateTime.parse(batchStatusRequestDto.getEndTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        BatchStatus batchStatus = BatchStatus
                .builder()
                .workflowId(batchStatusRequestDto.getWorkflowId())
                .workflowName(batchStatusRequestDto.getWorkflowName())
                .status(batchStatusRequestDto.getStatus())
                .duration(batchStatusRequestDto.getDuration())
                .startTime(startTime.toLocalDateTime())
                .endTime(endTime.toLocalDateTime())
                .failedStepName(batchStatusRequestDto.getFailedStepName())
                .errMsg(batchStatusRequestDto.getErrMsg())
                .build();
        batchStatusRepository.save(batchStatus);
    }

    public ExchangeListResponseDto findExchangeList(ListSearchRequestDto listSearchRequestDto) {
        Pageable pageable;
        Page<Exchange> exchangePage;

        if (listSearchRequestDto != null && listSearchRequestDto.getPageBaseDto() != null) {
            pageable = PageRequest.of(
                    Math.max(0, listSearchRequestDto.getPageBaseDto().getCurrentPage() - 1),
                    listSearchRequestDto.getPageBaseDto().getPerPage(),
                    Sort.by(Sort.Direction.ASC, "name")
            );
        } else {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "name"));
        }

        String keyword = listSearchRequestDto != null ? listSearchRequestDto.getKeyword() : null;

        exchangePage = (keyword != null && !keyword.trim().isEmpty())
                ? exchangeRepository.findAllByNameContaining(keyword.trim(), pageable)
                : exchangeRepository.findAll(pageable);

        List<Exchange> exchanges = exchangePage.getContent();

        return exchanges.size() > 0 ?
                ExchangeListResponseDto.builder()
                        .code(ResultCode.SUCCESS.getCode())
                        .exchangeDtoList(
                                exchanges.stream().map((it) -> (
                                        ExchangeListResponseDto.ExchangeDto.builder()
                                                .name(it.getName())
                                                .unit(it.getUnit())
                                                .krUnit(it.getKrUnit())
                                                .dealBasR(it.getDealBasR())
                                                .displayUnit(it.getDisplayUnit())
                                                .updatedAt(it.getUpdatedAt())
                                                .build()
                                )).collect(Collectors.toList())
                        )
                        .build()
                : ExchangeListResponseDto.builder()
                .code(ResultCode.NO_CONTENT.getCode())
                .build();
    }

    public Integer findExchangeListCount() {
        return Math.toIntExact(exchangeRepository.count());
    }

    public void saveExchangeList(List<ExchangeWebApiResponseDto> exchangeWebApiResponseDtoList) {
        for (ExchangeWebApiResponseDto dto : exchangeWebApiResponseDtoList) {
            exchangeRepository.save(ExchangeWebApiResponseDto.toExchange(dto));
        }
    }

    public void saveScrapExchange(ExchangeScrapResponseDto exchangeScrapResponseDto) {
        if (exchangeScrapResponseDto != null) {
            exchangeRepository.save(ExchangeScrapResponseDto.toExchange(exchangeScrapResponseDto));
        }
    }

    public void forceSaveExchangeList(String date) {

    }


}
