package ru.Krivi4.Krivi4LibraryRest.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.Krivi4.Krivi4LibraryRest.models.Book;
import ru.Krivi4.Krivi4LibraryRest.models.Reader;
import ru.Krivi4.Krivi4LibraryRest.repositories.BooksRepository;
import ru.Krivi4.Krivi4LibraryRest.util.StubData;
import ru.Krivi4.Krivi4LibraryRest.web.exceptions.BookNotFoundException;
import ru.Krivi4.Krivi4LibraryRest.web.exceptions.OwnerNotFoundException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultBooksServiceTest {

    @Mock
    private BooksRepository booksRepository;

    @InjectMocks
    private DefaultBooksService bookService;

    private Book book;
    private Reader reader;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setYear(2020);

        reader = new Reader("Иванов Иван Иванович",
                LocalDate.of(1990, 1, 1), "ivan@example.com");
        reader.setId(1);
    }

    @Test
    void findAll_ReturnsBooks() {
        Page<Book> page = new PageImpl<>(Collections.singletonList(book));
        when(booksRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Book> result = bookService.findAll(PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
        assertEquals("Test Book", result.getContent().get(0).getTitle());
    }

    @Test
    void findById_ReturnsBook() {
        when(booksRepository.findById(1)).thenReturn(Optional.of(book));

        Book result = bookService.findById(1);

        assertEquals("Test Book", result.getTitle());
    }

    @Test
    void findById_ThrowsException_WhenNotFound() {
        when(booksRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findById(1));
    }

    @Test
    void save_SavesBook() {
        when(booksRepository.save(book)).thenReturn(book);

        Book result = bookService.save(book);

        assertEquals("Test Book", result.getTitle());
        assertNotNull(result.getAddedAt());
    }

    @Test
    void update_UpdatesBook() {
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Book");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setYear(2021);
        when(booksRepository.save(updatedBook)).thenReturn(updatedBook);

        Book result = bookService.update(1, updatedBook);

        assertEquals(1, result.getId());
        assertEquals("Updated Book", result.getTitle());
    }

    @Test
    void delete_DeletesBook() {
        bookService.delete(1);

        verify(booksRepository).deleteById(1);
    }

    @Test
    void searchByTitle_ReturnsBooks() {
        List<Book> books = Collections.singletonList(book);
        when(booksRepository.findByTitleStartingWithIgnoreCase("Test")).thenReturn(books);

        List<Book> result = bookService.searchByTitle("Test");

        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
    }

    @Test
    void getBookOwner_ReturnsOwner() {
        book.setOwner(reader);
        when(booksRepository.findById(1)).thenReturn(Optional.of(book));

        Reader result = bookService.getBookOwner(1);

        assertEquals("Иванов Иван Иванович", result.getFullName());
    }

    @Test
    void getBookOwner_ThrowsException_WhenNoOwner() {
        book.setOwner(null);
        when(booksRepository.findById(1)).thenReturn(Optional.of(book));

        assertThrows(OwnerNotFoundException.class, () -> bookService.getBookOwner(1));
    }

    @Test
    void appoint_SetsOwner() {
        when(booksRepository.findById(1)).thenReturn(Optional.of(book));

        bookService.appoint(1, reader);

        assertEquals(reader, book.getOwner());
        assertNotNull(book.getTakenAt());
    }

    @Test
    void release_SetsPlaceholderReaderAndDefaultTakenAt() {

        book.setOwner(reader);
        when(booksRepository.findById(book.getId())).thenReturn(Optional.of(book));


        bookService.release(book.getId());


        assertNotNull(book.getOwner(),
                "Владелец книги не должен быть null");
        assertEquals(StubData.NO_OWNER.getId(), book.getOwner().getId(),
                "ID владельца должен быть 0");
        assertEquals(StubData.NO_OWNER.getFullName(), book.getOwner().getFullName(),
                "Имя владельца должно быть 'Книга свободна'");
        assertEquals(StubData.NO_OWNER.getEmail(), book.getOwner().getEmail(),
                "Email владельца должен быть 'no-reply@example.com'");
        assertEquals(StubData.NO_OWNER.getDateOfBirth(), book.getOwner().getDateOfBirth(),
                "Дата рождения владельца должна быть 1900-01-01");
        assertEquals(StubData.DEFAULT_TAKEN_AT, book.getTakenAt(),
                "Время взятия книги должно быть DEFAULT_TAKEN_AT");

        verify(booksRepository).findById(book.getId());
    }
}