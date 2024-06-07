package com.my.batch.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum MsgTemplateType {

    CERTIFICATION("certification"),
    RESERVATION("reservation");

    private final String name;

    public static MsgTemplateType of(final String name) {
        for (MsgTemplateType msgTemplateType : values()) {
            if (msgTemplateType.name.equals(name)) {
                return msgTemplateType;
            }
        }
        return null;
    }

}
