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
import ru.Krivi4.Krivi4LibraryRest.models.Reader;
import ru.Krivi4.Krivi4LibraryRest.repositories.ReadersRepository;
import ru.Krivi4.Krivi4LibraryRest.web.exceptions.ReaderDuplicateEmailException;
import ru.Krivi4.Krivi4LibraryRest.web.exceptions.ReaderNotFoundException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultReadersServiceTest {

    @Mock
    private ReadersRepository readersRepository;

    @InjectMocks
    private DefaultReadersService readerService;

    private Reader reader;

    /** Установка параметров читателя (перед каждым тестом)*/
    @BeforeEach
    void setUp() {
        reader = new Reader("Иванов Иван Иванович",
                LocalDate.of(1990, 1, 1), "ivan@example.com");
        reader.setId(1);
    }

    /** Тесть поиска всех читателей */
    @Test
    void findAll_ReturnsReaders() {
        Page<Reader> page = new PageImpl<>(Collections.singletonList(reader));
        when(readersRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Reader> result = readerService.findAll(PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
        assertEquals("Иванов Иван Иванович", result.getContent().get(0).getFullName());
    }

    /** Тесть поиска читателя по id - упешно*/
    @Test
    void findById_ReturnsReader() {
        when(readersRepository.findById(1)).thenReturn(Optional.of(reader));

        Reader result = readerService.findById(1);

        assertEquals("Иванов Иван Иванович", result.getFullName());
    }

    /** Тесть поиска читателя по id - не найдено */
    @Test
    void findById_ThrowsException_WhenNotFound() {
        when(readersRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ReaderNotFoundException.class, () -> readerService.findById(1));
    }

    /** Тест сохранения читателя */
    @Test
    void save_SavesReader() {
        when(readersRepository.existsByEmail("ivan@example.com")).thenReturn(false);
        when(readersRepository.save(reader)).thenReturn(reader);

        Reader result = readerService.save(reader);

        assertEquals("Иванов Иван Иванович", result.getFullName());
        assertNotNull(result.getCreatedAt());
    }

    /** Тест сохранения читателя, если emil у занят*/
    @Test
    void save_ThrowsException_WhenEmailExists() {
        when(readersRepository.existsByEmail("ivan@example.com")).thenReturn(true);

        assertThrows(ReaderDuplicateEmailException.class, () -> readerService.save(reader));
    }

    /** Тесть обновления читателя*/
    @Test
    void update_UpdatesReader() {
        Reader updatedReader = new Reader("Петров Петр Петрович", LocalDate.of(1985, 1, 1), "petr@example.com");
        when(readersRepository.existsByEmailAndIdNot("petr@example.com", 1)).thenReturn(false);
        when(readersRepository.save(updatedReader)).thenReturn(updatedReader);

        Reader result = readerService.update(1, updatedReader);

        assertEquals(1, result.getId());
        assertEquals("Петров Петр Петрович", result.getFullName());
    }

    /** Тест удаления читателя*/
    @Test
    void delete_DeletesReader() {
        readerService.delete(1);

        verify(readersRepository).deleteById(1);
    }
}