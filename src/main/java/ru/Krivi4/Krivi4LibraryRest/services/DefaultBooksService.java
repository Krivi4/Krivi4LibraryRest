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


@Service
@RequiredArgsConstructor
public class DefaultBooksService implements BookService {

    private final BooksRepository booksRepository;


    /** Выводит все книги */
    @Override
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        return booksRepository.findAll(pageable);
    }
    /** Выводит одну книгу по id */
    @Override
    @Transactional(readOnly = true)
    public Book findById(int id) {
        return booksRepository
                .findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    /** Выводит все книги подходящие под запрос */
    @Override
    @Transactional(readOnly = true)
    public List<Book> searchByTitle(String query)
    {
        return booksRepository.findByTitleStartingWithIgnoreCase(query);
    }

    /** Выводит владельца книги */
    @Override
    @Transactional(readOnly = true)
    public Reader getBookOwner(int id) {
        return booksRepository
                .findById(id)
                .map(Book::getOwner)
                .orElseThrow(() -> new OwnerNotFoundException(id));
    }

    /** Сохраняет книгу (если не указаны какие-то параметры: закрывает их заглушками)
     * +устанавливает время назначения книги читателю */
    @Override
    @Transactional
    public Book save(Book book) {
        applyStubs(book);
        enrichBook(book);
        booksRepository.save(book);
        return book;
    }

    /** Обновляет данные книги (если не указаны какие-то параметры: закрывает их заглушками)
     * + устанавливает время назначения книги читателю */
    @Override
    @Transactional
    public Book update(int id, Book updatedBook) {
        updatedBook.setId(id);
        applyStubs(updatedBook);
        enrichBook(updatedBook);
        booksRepository.save(updatedBook);

        return updatedBook;
    }

    /** Удаляет книгу */
    @Override
    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    /** Освобождает книгу от владельца */
    @Override
    @Transactional
    public void release(int id) {
        booksRepository
                .findById(id)
                .ifPresent(book -> {
            book.setOwner(null);
            book.setTakenAt(null);
        });

    }

    /** Назначает книгу читателю */
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

    /** Устанавливает время взятия книги при назначении */
    private void enrichBook(Book book) {
        book.setAddedAt(LocalDateTime.now());
    }

    /** Устанавливает заглушки если не были указаны какие-то параметры при запросе */
    private void applyStubs(Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            book.setTitle("Название не указано");
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            book.setAuthor("Автор не указан");
        }

        Integer year = book.getYear();
        if (book.getYear() == null) {
            book.setYear(0);
        } else if (year < 0 || year > LocalDate.now().getYear()) {
            throw new IllegalArgumentException(
                    "Год должен быть в диапазоне от 0 до " + LocalDate.now().getYear());
        }

    }

}

