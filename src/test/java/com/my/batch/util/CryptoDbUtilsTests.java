package com.my.batch.util;

import com.my.batch.common.utils.CryptoDbUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
class CryptoDbUtilsTests {

    @Resource(name = "cryptoDbUtil")
    private CryptoDbUtil cryptoDbUtil;

    @Test
    void sendSms() throws Exception {

        // Given
        String plainText = "01011111111";

        // When
        String encrypt = cryptoDbUtil.encrypt(plainText);
        String decrypt = cryptoDbUtil.decrypt(encrypt);
        System.out.println("encrypt : " + encrypt);
        System.out.println("decrypt : " + decrypt);

        // Then
        assertThatNoException();
    }

}
