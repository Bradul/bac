package org.example.exceptions;

public class TimeLimitExceededException extends Exception {
    public TimeLimitExceededException() {
        super();
    }

    public TimeLimitExceededException (String message) {
        super(message);
    }

    public TimeLimitExceededException (Exception e) {
        super(e);
    }
}
