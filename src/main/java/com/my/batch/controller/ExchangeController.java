package com.my.batch.controller;

import com.my.batch.annotation.LoginUser;
import com.my.batch.domain.Member;
import com.my.batch.dto.common.PageBaseDto;
import com.my.batch.dto.exchange.request.BatchStatusRequestDto;
import com.my.batch.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    /**
     * @param batchStatusRequestDto
     * @apiNote 파이썬에서 넘어온 배치 상태 저장
     */
    @PostMapping("/batch-status")
    public ResponseEntity createBatchStatus(@RequestBody BatchStatusRequestDto batchStatusRequestDto) {
        exchangeService.saveBatchStatus(batchStatusRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param member
     * @apiNote 환율 리스트
     */
    @GetMapping("")
    public ResponseEntity findExchangeList(@LoginUser Member member) {
        return new ResponseEntity<>(exchangeService.findExchangeList(null), HttpStatus.OK);
    }

    /**
     * @param member
     * @param pageBaseDto
     * @apiNote 환율 리스트 With 페이지네이션
     */
    @PostMapping("")
    public ResponseEntity findExchangeListWithPagination(@LoginUser Member member, @RequestBody PageBaseDto pageBaseDto) {
        return new ResponseEntity<>(exchangeService.findExchangeList(pageBaseDto), HttpStatus.OK);
    }

    /**
     * @param member
     * @apiNote 환율 리스트 전체 수
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> findExchangeListCount(@LoginUser Member member) {
        return new ResponseEntity<>(exchangeService.findExchangeListCount(), HttpStatus.OK);
    }

}
