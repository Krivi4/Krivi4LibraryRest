package ru.Krivi4.Krivi4LibraryRest.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
public class ReaderDTO {
    @Pattern(regexp = "^[А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+$", message = "Введите ФИО в формате: Иванов Иван Иванович")
    @Size(min = 4, max = 100, message = "ФИО не может быть меньше 4 и больше 100")
    private String fullName;

    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Дата рождения должна быть в прошлом")
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;

    @NotEmpty(message = "Email не может быть пустым")
    @Email(message = "Введите верный формат Email")
    private String email;

}
