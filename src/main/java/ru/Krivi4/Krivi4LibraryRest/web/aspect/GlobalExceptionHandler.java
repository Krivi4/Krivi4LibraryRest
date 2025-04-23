package ru.Krivi4.Krivi4LibraryRest.web.aspect;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.Krivi4.Krivi4LibraryRest.views.exceptions.BookErrorResponse;
import ru.Krivi4.Krivi4LibraryRest.views.exceptions.ReaderErrorResponse;
import ru.Krivi4.Krivi4LibraryRest.web.exceptions.*;


import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Book Exceptions

    /** Книга по id не найдена */
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<BookErrorResponse> handleBookNotFound(BookNotFoundException ex) {
        BookErrorResponse resp = new BookErrorResponse(
                "Книга с id: " + ex.getId() + " не найдена",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
    }

    /** Обрабатывает исключения при некорректных операциях с книгой */
    @ExceptionHandler({BookNotCreatedException.class, BookNotUpdatedException.class, BookNotAppointException.class})
    public ResponseEntity<BookErrorResponse> handleBookBadRequest(RuntimeException ex) {
        BookErrorResponse resp = new BookErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    /** Вылавливает запросы, которые не прошли валидацию */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BookErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        BookErrorResponse resp = new BookErrorResponse(
                msg,
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
    /** При выводе владельца у свободной книги */
    @ExceptionHandler(OwnerNotFoundException.class)
    public ResponseEntity<ReaderErrorResponse> handleOwnerNotFound(OwnerNotFoundException ex) {
        ReaderErrorResponse resp = new ReaderErrorResponse(
                "Книга с id: " + ex.getId() + " свободна",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
    }


    // --- Reader Exceptions ---

    /** Читатель по id не найден */
    @ExceptionHandler(ReaderNotFoundException.class)
    public ResponseEntity<ReaderErrorResponse> handleReaderNotFound(ReaderNotFoundException ex) {
        ReaderErrorResponse resp = new ReaderErrorResponse(
                "Читатель с id: " + ex.getId() + " не найден",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
    }
    /** Обрабатывает исключения при некорректных операциях с читателем */
    @ExceptionHandler({ReaderNotCreatedException.class, ReaderNotUpdatedException.class})
    public ResponseEntity<ReaderErrorResponse> handleReaderBadRequest(RuntimeException ex) {
        ReaderErrorResponse resp = new ReaderErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    /** При обновлении или добавлении читателя с имеющимся в бд email */
    @ExceptionHandler(ReaderDuplicateEmailException.class)
    public ResponseEntity<ReaderErrorResponse> handleDuplicateEmail(ReaderDuplicateEmailException ex) {
        ReaderErrorResponse resp = new ReaderErrorResponse(
                "Пользователь с Email: " + ex.getEmail() + " уже существует",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);

    }
}