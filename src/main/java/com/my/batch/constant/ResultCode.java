package com.my.batch.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS("1000", "success", HttpStatus.OK),
    NO_CONTENT("1001", "no-content", HttpStatus.OK),
    INVALID("E001", "invalid", HttpStatus.BAD_REQUEST),
    NOT_FOUND_USER("E002", "not-found-user", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("E003", "unauthorized", HttpStatus.UNAUTHORIZED),
    CRYPTO_ERROR("E004", "crypto-error", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_ALLOWED_USER("E005", "not-allowed-user", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("E006", "invalid-token", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_SCRAP("E007", "unauthorized-scrap", HttpStatus.UNAUTHORIZED),
    DUPLICATED("E008", "duplicated-userId-or-regNo", HttpStatus.BAD_REQUEST),
    UNCAUGHT("E999", "system-error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;

    private final String message;

    private final HttpStatus httpStatus;

}
