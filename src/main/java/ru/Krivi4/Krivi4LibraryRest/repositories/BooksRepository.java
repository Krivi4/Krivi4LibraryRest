package ru.Krivi4.Krivi4LibraryRest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Krivi4.Krivi4LibraryRest.models.Book;

import java.util.List;

@Repository
public interface BooksRepository
        extends JpaRepository<Book, Integer>{
    List<Book> findByTitleStartingWithIgnoreCase(String title);
}