package ru.Krivi4.Krivi4LibraryRest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.Krivi4.Krivi4LibraryRest.models.Reader;


public interface ReaderService {
    Page<Reader> findAll(Pageable pageable);
    Reader findById(int id);
    Reader save(Reader reader);
    Reader update(int id, Reader updatedReader);
    void delete(int id);
}