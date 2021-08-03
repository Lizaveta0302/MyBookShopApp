package org.app.exception;

import java.io.FileNotFoundException;

public class UploadFileException extends FileNotFoundException {
    private final String message;

    public UploadFileException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
