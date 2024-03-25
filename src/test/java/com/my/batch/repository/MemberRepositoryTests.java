package com.my.batch.repository;

import com.my.batch.domain.MemberExchange;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MemberRepositoryTests {

    @Resource(name = "memberExchangeRepository")
    private MemberExchangeRepository memberExchangeRepository;

    @Test
    void findAll() {

        // When
        List<MemberExchange> memberExchanges = memberExchangeRepository.findAll();
        System.out.println(memberExchanges);

        // Then
        assertNotNull(memberExchanges);
    }

}
