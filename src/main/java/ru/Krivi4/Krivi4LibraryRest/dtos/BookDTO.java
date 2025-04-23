package ru.Krivi4.Krivi4LibraryRest.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class BookDTO {

    private String title;

    @Size(min = 5, max = 50, message = "Инициалы автора книги не могут быть меньше 5 и больше 50")
    private String author;

    @Min(value = 0, message = "Год не может быть меньше 0")
    @Max(value = 2025, message = "Год не может быть больше текущего")
    private Integer year;

}
