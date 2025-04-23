package ru.Krivi4.Krivi4LibraryRest.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reader")
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class Reader {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private int id;

    @Column(name = "reader_ticket_number", insertable = false, updatable = false)
    @ToString.Include
    private int readerTicketNumber;

    @Column(name = "full_name")
    @Pattern(regexp = "^[А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+$", message = "Введите ФИО в формате: Иванов Иван Иванович")
    @Size(min = 4, max = 100, message = "ФИО не может быть меньше 4 и больше 100")
    @ToString.Include
    @NonNull
    private String fullName;



    @Column(name = "date_of_birth")
    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Дата рождения должна быть в прошлом")
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd.MM.yyyy")
    @ToString.Include
    @NonNull
    private LocalDate dateOfBirth;


    @Column(name = "email")
    @NotEmpty(message = "Email не может быть пустым")
    @Email(message = "Введите верный формат Email")
    @ToString.Include
    @NonNull
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;


}
