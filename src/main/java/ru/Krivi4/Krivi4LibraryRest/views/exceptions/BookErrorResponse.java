package ru.Krivi4.Krivi4LibraryRest.views.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookErrorResponse {

    private String message;
    private Long timestamp;

}