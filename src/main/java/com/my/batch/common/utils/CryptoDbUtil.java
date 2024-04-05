package com.my.batch.common.utils;

import com.my.batch.exception.error.CryptoErrorException;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class CryptoDbUtil {

    @Value("${security.crypto-db-iv}")
    private String cryptoDbIv; // 256 bit

    @Value("${security.crypto-db-key}")
    private String cryptoDbKey; // 256 bit

    public String encrypt(String msg) throws Exception {
        try {
            return encryptAes(msg, cryptoDbKey, cryptoDbIv);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CryptoErrorException();
        }
    }

    public String decrypt(String msg) throws Exception {
        try {
            return decryptAes(msg, cryptoDbKey, cryptoDbIv);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CryptoErrorException();
        }
    }

    private String encryptAes(String plainText, String inputKey, String inputIV) throws Exception {
        byte[] IV = DatatypeConverter.parseHexBinary(inputIV);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(inputKey.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));

        byte[] result = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printHexBinary(result);
    }

    private String decryptAes(String text, String inputKey, String inputIV) throws Exception {
        byte[] cipherText = DatatypeConverter.parseHexBinary(text);
        byte[] IV = DatatypeConverter.parseHexBinary(inputIV);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(inputKey.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));

        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
    }
}
