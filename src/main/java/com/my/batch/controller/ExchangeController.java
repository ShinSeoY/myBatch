package com.my.batch.controller;

import com.my.batch.annotation.LoginUser;
import com.my.batch.domain.Member;
import com.my.batch.dto.common.PageBaseDto;
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

    @GetMapping("")
    public ResponseEntity findExchangeList(@LoginUser Member member) {
        return new ResponseEntity<>(exchangeService.findExchangeList(null), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity findExchangeListWithPagination(@LoginUser Member member, @RequestBody PageBaseDto pageBaseDto) {
        return new ResponseEntity<>(exchangeService.findExchangeList(pageBaseDto), HttpStatus.OK);
    }

}
