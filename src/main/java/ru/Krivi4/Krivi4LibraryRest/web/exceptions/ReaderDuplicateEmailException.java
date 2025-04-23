package ru.Krivi4.Krivi4LibraryRest.web.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ReaderDuplicateEmailException extends RuntimeException     {
    private final String email;

    }

