package com.my.batch.controller;

import com.my.batch.annotation.LoginUser;
import com.my.batch.domain.Member;
import com.my.batch.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping("")
    public ResponseEntity findExchangeList(@LoginUser Member member) {
        return new ResponseEntity<>(exchangeService.findExchangeList(), HttpStatus.OK);
    }

}
