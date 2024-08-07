package com.my.batch.service;

import com.my.batch.common.utils.ExchangeUtils;
import com.my.batch.constant.ResultCode;
import com.my.batch.domain.BatchStatus;
import com.my.batch.domain.Exchange;
import com.my.batch.dto.common.PageBaseDto;
import com.my.batch.dto.exchange.request.BatchStatusRequestDto;
import com.my.batch.dto.exchange.response.ExchangeListResponseDto;
import com.my.batch.dto.exchange.response.ExchangeScrapResponseDto;
import com.my.batch.dto.exchange.response.ExchangeWebApiResponseDto;
import com.my.batch.repository.BatchStatusRepository;
import com.my.batch.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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

    public ExchangeListResponseDto findExchangeList(PageBaseDto pageBaseDto) {
        List<Exchange> exchanges;
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        if (pageBaseDto != null) {
            PageRequest pageRequest = PageRequest.of(pageBaseDto.getCurrentPage() - 1, pageBaseDto.getPerPage(), sort);
            exchanges = exchangeRepository.findAll(pageRequest).stream().toList();
        } else {
            exchanges = exchangeRepository.findAll(sort);
        }
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
