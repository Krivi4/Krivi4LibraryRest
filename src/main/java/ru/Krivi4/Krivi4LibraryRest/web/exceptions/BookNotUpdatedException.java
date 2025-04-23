package ru.Krivi4.Krivi4LibraryRest.web.exceptions;

public class BookNotUpdatedException extends RuntimeException {
    public BookNotUpdatedException(String message) {
        super(message);
    }
}