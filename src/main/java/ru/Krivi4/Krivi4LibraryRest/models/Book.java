package ru.Krivi4.Krivi4LibraryRest.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private int id;

    @Column(name = "title")
    @ToString.Include
    private String title;


    @Column(name = "author")
    @Size(min = 5, max = 50, message = "Инициалы автора книги не могут быть меньше 5 и больше 50")
    @ToString.Include
    private String author;

    @Column(name = "year")
    @Min(value = 0, message = "Год не может быть меньше 0")
    @Max(value = 2025, message = "Год не может быть больше текущего")
    @ToString.Include
    private Integer year;


    @ManyToOne
    @JoinColumn(name = "reader_id", referencedColumnName = "id")
    private Reader owner;

    @Column(name = "taken_at")
    private LocalDateTime takenAt;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @Transient
    private boolean expired;

}
