package org.example.exceptions;

public class IllegalAnswerFormatException extends Exception {

    public IllegalAnswerFormatException() {
        super();
    }

    public IllegalAnswerFormatException (String message) {
        super(message);
    }

    public IllegalAnswerFormatException (Exception e) {
        super(e);
    }
}
