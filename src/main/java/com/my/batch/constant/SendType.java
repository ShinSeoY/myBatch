package com.my.batch.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum SendType {

    SENDING("sending"),
//    COMPLETED("completed"),
    FAILED("failed"),

    SEND_SUCCESS("send-success"),
    SEND_FAIL("send-fail");

    private final String name;

    public static SendType of(final String name) {
        for (SendType sendType : values()) {
            if (sendType.name.equals(name)) {
                return sendType;
            }
        }
        return null;
    }

}
