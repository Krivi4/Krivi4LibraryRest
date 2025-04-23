package ru.Krivi4.Krivi4LibraryRest.views;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponse {

    private String title;
    private String author;
    private Integer year;

}
