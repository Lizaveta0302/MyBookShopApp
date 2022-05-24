package com.example.bookshop_app.entity.book;

import java.util.stream.Stream;

public enum Status {
    PAID(0),
    ARCHIVED(1);

    private final int code;

    Status(int code) {
        this.code = code;
    }

    public static Status getByCode(int code) {
        return Stream.of(Status.values()).filter(s -> s.getCode() == code).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }
}
