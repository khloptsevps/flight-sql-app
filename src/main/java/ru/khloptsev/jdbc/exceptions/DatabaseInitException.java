package ru.khloptsev.jdbc.exceptions;

public class DatabaseInitException extends RuntimeException {
    public DatabaseInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
