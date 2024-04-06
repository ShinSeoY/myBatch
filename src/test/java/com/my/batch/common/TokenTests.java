package com.my.batch.common;

import com.my.batch.common.security.AuthenticationTokenProvider;
import com.my.batch.domain.Member;
import com.my.batch.repository.MemberRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TokenTests {

    @Resource(name = "authenticationTokenProvider")
    private AuthenticationTokenProvider authenticationTokenProvider;

    @Resource(name = "memberRepository")
    private MemberRepository memberRepository;

    @Test
    void generateJwtToken() {

        // Given
        String email = "shsy312@gmail.com";
        Member member = memberRepository.findByEmail(email).orElseThrow();

        // When
        String jwt = authenticationTokenProvider.generateJwtToken(member);

        System.out.println(jwt);

        // Then
        assertNotNull(jwt);
    }

}
