package org.example.exceptions;

public class UnrecognizedLanguageException extends Exception {
    public UnrecognizedLanguageException() {
        super();
    }

    public UnrecognizedLanguageException(String message) {
        super(message);
    }

    public UnrecognizedLanguageException(Exception e) {
        super(e);
    }
}
