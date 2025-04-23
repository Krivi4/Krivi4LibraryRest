package ru.Krivi4.Krivi4LibraryRest.web.exceptions;

public class BookNotCreatedException extends RuntimeException {
    public BookNotCreatedException(String message) {
        super(message);
    }
}