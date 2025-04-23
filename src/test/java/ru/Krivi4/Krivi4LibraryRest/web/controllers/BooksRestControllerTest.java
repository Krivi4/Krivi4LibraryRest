package ru.Krivi4.Krivi4LibraryRest.web.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.Krivi4.Krivi4LibraryRest.dtos.BookDTO;
import ru.Krivi4.Krivi4LibraryRest.mappers.dto.BookDTOMapper;
import ru.Krivi4.Krivi4LibraryRest.mappers.view.BookViewMapper;
import ru.Krivi4.Krivi4LibraryRest.mappers.view.ReaderViewMapper;
import ru.Krivi4.Krivi4LibraryRest.models.Book;
import ru.Krivi4.Krivi4LibraryRest.models.Reader;
import ru.Krivi4.Krivi4LibraryRest.services.BookService;
import ru.Krivi4.Krivi4LibraryRest.services.ReaderService;
import ru.Krivi4.Krivi4LibraryRest.views.BookResponse;
import ru.Krivi4.Krivi4LibraryRest.views.ReaderResponse;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BooksRestController.class)
class BooksRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private ReaderService readerService;

    @MockBean
    private BookDTOMapper bookDTOMapper;

    @MockBean
    private BookViewMapper bookViewMapper;

    @MockBean
    private ReaderViewMapper readerViewMapper;

    private Book book;
    private BookDTO bookDTO;
    private BookResponse bookResponse;
    private Reader reader;
    private ReaderResponse readerResponse;

    /** Установка параметров модели, DTO, View книги и читателя (перед каждым тестом)*/
    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setYear(2020);

        bookDTO = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setYear(2020);

        bookResponse = new BookResponse();
        bookResponse.setTitle("Test Book");
        bookResponse.setAuthor("Test Author");
        bookResponse.setYear(2020);

        reader = new Reader("Иванов Иван Иванович",
                LocalDate.of(1990, 1, 1), "ivan@example.com");
        reader.setId(1);

        readerResponse = new ReaderResponse();
        readerResponse.setFullName("Иванов Иван Иванович");
        readerResponse.setDateOfBirth(LocalDate.of(1990, 1, 1));
        readerResponse.setEmail("ivan@example.com");
    }

    /** Тест вывода всех книг */
    @Test
    void getBooks_ReturnsBooks() throws Exception {
        Page<Book> page = new PageImpl<>(Collections.singletonList(book));
        when(bookService.findAll(any(Pageable.class))).thenReturn(page);
        when(bookViewMapper.toResponse(book)).thenReturn(bookResponse);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }

    /** Тест вывода книги по id*/
    @Test
    void getBook_ReturnsBook() throws Exception {
        when(bookService.findById(1)).thenReturn(book);
        when(bookViewMapper.toResponse(book)).thenReturn(bookResponse);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }
    /** Тест создания книги */
    @Test
    void create_CreatesBook() throws Exception {
        when(bookDTOMapper.toEntity(any(BookDTO.class))).thenReturn(book);
        when(bookService.save(any(Book.class))).thenReturn(book);
        when(bookViewMapper.toResponse(book)).thenReturn(bookResponse);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Book\",\"author\":\"Test Author\",\"year\":2020}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }
    /** Тест обновления книги */
    @Test
    void update_UpdatesBook() throws Exception {
        when(bookDTOMapper.toEntity(any(BookDTO.class))).thenReturn(book);
        when(bookService.update(eq(1), any(Book.class))).thenReturn(book);
        when(bookViewMapper.toResponse(book)).thenReturn(bookResponse);

        mockMvc.perform(put("/books/1/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Book\",\"author\":\"Test Author\",\"year\":2020}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    /** Тест удаления книги */
    @Test
    void delete_DeletesBook() throws Exception {
        when(bookService.findById(1)).thenReturn(book);
        when(bookViewMapper.toResponse(book)).thenReturn(bookResponse);

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    /** Тест назначения книги читателю*/
    @Test
    void appoint_AppointsReader() throws Exception {
        when(bookService.findById(1)).thenReturn(book);
        when(readerService.findById(1)).thenReturn(reader);
        when(bookViewMapper.toResponse(book)).thenReturn(bookResponse);
        when(readerViewMapper.toResponse(reader)).thenReturn(readerResponse);

        mockMvc.perform(put("/books/1/appoint?readerId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    /** Тест освобождения книги от читателя */
    @Test
    void release_ReleasesBook() throws Exception {
        // Подготовка: при запросе книги по id возвращаем наш тестовый объект
        when(bookService.findById(1)).thenReturn(book);
        // Маппер преобразует модель в объект ответа
        when(bookViewMapper.toResponse(book)).thenReturn(bookResponse);

        // Выполняем PUT-запрос на /books/1/release
        mockMvc.perform(put("/books/1/release"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageBook").value("Книга с данными ниже освобождена "))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.year").value(2020));
    }
}