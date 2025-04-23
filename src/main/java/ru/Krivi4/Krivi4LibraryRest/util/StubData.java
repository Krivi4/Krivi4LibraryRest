package ru.Krivi4.Krivi4LibraryRest.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.Krivi4.Krivi4LibraryRest.models.Reader;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StubData {

    public static final String DEFAULT_TITLE  = "Название не указано";
    public static final String DEFAULT_AUTHOR = "Автор не указан";
    public static final Integer DEFAULT_YEAR = 1;

    // Заглушка “Книга свободна”
    public static final Reader NO_OWNER = createNoOwner();
    // Заглушка “время взятия”
    public static final LocalDateTime DEFAULT_TAKEN_AT =
            LocalDateTime.of(1, 1, 1, 0, 0);


    private static Reader createNoOwner() {
        Reader stub = new Reader(
                "Книга свободна",
                LocalDate.of(1900, 1, 1),
                "no-reply@example.com");
        stub.setId(0);
        return stub;
    }
}
