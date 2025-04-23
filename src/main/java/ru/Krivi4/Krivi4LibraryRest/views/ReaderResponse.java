package ru.Krivi4.Krivi4LibraryRest.views;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReaderResponse {

    private String fullName;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;

    private String email;

}
