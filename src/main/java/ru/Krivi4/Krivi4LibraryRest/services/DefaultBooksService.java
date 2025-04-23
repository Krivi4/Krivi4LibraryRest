package ru.Krivi4.Krivi4LibraryRest.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Krivi4.Krivi4LibraryRest.models.Book;
import ru.Krivi4.Krivi4LibraryRest.models.Reader;
import ru.Krivi4.Krivi4LibraryRest.repositories.BooksRepository;
import ru.Krivi4.Krivi4LibraryRest.web.exceptions.BookNotFoundException;
import ru.Krivi4.Krivi4LibraryRest.web.exceptions.OwnerNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.Krivi4.Krivi4LibraryRest.util.StubData.*;

@Service
@RequiredArgsConstructor
public class DefaultBooksService implements BookService {

    private final BooksRepository booksRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        return booksRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(int id) {
        return booksRepository
                .findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> searchByTitle(String query)
    {
        return booksRepository.findByTitleStartingWithIgnoreCase(query);
    }

    @Override
    @Transactional(readOnly = true)
    public Reader getBookOwner(int id) {
        return booksRepository
                .findById(id)
                .map(Book::getOwner)
                .orElseThrow(() -> new OwnerNotFoundException(id));
    }

    @Override
    @Transactional
    public Book save(Book book) {
        applyStubs(book);
        enrichBook(book);
        booksRepository.save(book);
        return book;
    }

    @Override
    @Transactional
    public Book update(int id, Book updatedBook) {
        updatedBook.setId(id);
        applyStubs(updatedBook);
        enrichBook(updatedBook);
        booksRepository.save(updatedBook);

        return updatedBook;
    }

    @Override
    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void release(int id) {
        booksRepository
                .findById(id)
                .ifPresent(book -> {
            book.setOwner(NO_OWNER);
            book.setTakenAt(DEFAULT_TAKEN_AT);
        });

    }

    @Override
    @Transactional
    public void appoint(int id, Reader selectedReader) {
        booksRepository
                .findById(id)
                .ifPresent(book -> {
            book.setOwner(selectedReader);
            book.setTakenAt(LocalDateTime.now());
        });
    }

    private void enrichBook(Book book) {
        book.setAddedAt(LocalDateTime.now());
    }

    private void applyStubs(Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            book.setTitle(DEFAULT_TITLE);
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            book.setAuthor(DEFAULT_AUTHOR);
        }

        Integer year = book.getYear();
        if (book.getYear() == null) {
            book.setYear(DEFAULT_YEAR);
        } else if (year < 0 || year > LocalDate.now().getYear()) {
            throw new IllegalArgumentException(
                    "Год должен быть в диапазоне от 0 до " + LocalDate.now().getYear());
        }
        if (book.getTakenAt() == null) {
            book.setTakenAt(DEFAULT_TAKEN_AT);
        }

    }

}

