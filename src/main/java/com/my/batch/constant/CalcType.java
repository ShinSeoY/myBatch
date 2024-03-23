package com.my.batch.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum CalcType {

    GTE("gte"),
    LTE("lte");

    private final String name;

    public static CalcType of(final String name) {
        for (CalcType calcType : values()) {
            if (calcType.name.equals(name)) {
                return calcType;
            }
        }
        return null;
    }

}
