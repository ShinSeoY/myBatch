package com.my.batch.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum MsgType {

    EMAIL("email"),
    SMS("sms");

    private final String name;

    public static MsgType of(final String name) {
        for (MsgType msgType : values()) {
            if (msgType.name.equals(name)) {
                return msgType;
            }
        }
        return null;
    }

}
