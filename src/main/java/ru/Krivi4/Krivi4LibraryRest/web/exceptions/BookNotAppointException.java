package ru.Krivi4.Krivi4LibraryRest.web.exceptions;

public class BookNotAppointException extends RuntimeException {
    public BookNotAppointException(String message) {
        super(message);
    }
}