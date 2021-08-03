package org.app.exception;

public class FilterOrRemoveByFieldException extends java.lang.NoSuchFieldException {
    private final String message;

    public FilterOrRemoveByFieldException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
