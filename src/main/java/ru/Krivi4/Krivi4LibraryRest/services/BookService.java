package ru.Krivi4.Krivi4LibraryRest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.Krivi4.Krivi4LibraryRest.models.Book;
import ru.Krivi4.Krivi4LibraryRest.models.Reader;

import java.util.List;

public interface BookService {
    Page<Book> findAll(Pageable pageable);
    Book findById(int id);
    List<Book> searchByTitle(String query);
    Reader getBookOwner(int id);
    Book save(Book book);
    Book update(int id, Book updatedBook);
    void delete(int id);
    void release(int id);
    void appoint(int id, Reader reader);
}