package org.example.exceptions;

public class CompilationErrorException extends Exception {

    public CompilationErrorException() {
        super();
    }

    public CompilationErrorException (String message) {
        super(message);
    }

    public CompilationErrorException (Exception e) {
        super(e);
    }
}