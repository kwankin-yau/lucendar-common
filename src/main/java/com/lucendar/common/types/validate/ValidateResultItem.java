package com.lucendar.common.types.validate;

import java.util.StringJoiner;

public class ValidateResultItem {
    private final ValidateResultItemType type;
    private final String message;

    public ValidateResultItem(ValidateResultItemType type, String message) {
        this.type = type;
        this.message = message;
    }

    public ValidateResultItemType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ValidateResultItem.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("message='" + message + "'")
                .toString();
    }
}
