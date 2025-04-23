package ru.Krivi4.Krivi4LibraryRest.web.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.Krivi4.Krivi4LibraryRest.dtos.ReaderDTO;
import ru.Krivi4.Krivi4LibraryRest.mappers.dto.ReaderDTOMapper;
import ru.Krivi4.Krivi4LibraryRest.mappers.view.ReaderViewMapper;
import ru.Krivi4.Krivi4LibraryRest.models.Reader;
import ru.Krivi4.Krivi4LibraryRest.services.ReaderService;
import ru.Krivi4.Krivi4LibraryRest.views.ReaderResponse;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReadersRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReaderService readerService;

    @MockBean
    private ReaderDTOMapper readerDTOMapper;

    @MockBean
    private ReaderViewMapper readerViewMapper;

    private Reader reader;
    private ReaderDTO readerDTO;
    private ReaderResponse readerResponse;

    @BeforeEach
    void setUp() {
        reader = new Reader("Иванов Иван Иванович",
                LocalDate.of(1990, 1, 1),
                "ivan@example.com");
        reader.setId(1);

        readerDTO = new ReaderDTO();
        readerDTO.setFullName("Иванов Иван Иванович");
        readerDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        readerDTO.setEmail("ivan@example.com");

        readerResponse = new ReaderResponse();
        readerResponse.setFullName("Иванов Иван Иванович");
        readerResponse.setDateOfBirth(LocalDate.of(1990, 1, 1));
        readerResponse.setEmail("ivan@example.com");
    }

    @Test
    void findAll_reader() throws Exception {
        Page<Reader> page = new PageImpl<>(Collections.singletonList(reader));
        when(readerService.findAll(any(Pageable.class))).thenReturn(page);
        when(readerViewMapper.toResponse(reader)).thenReturn(readerResponse);

        mockMvc.perform(get("/readers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Иванов Иван Иванович"));
    }

    @Test
    void findById_reader() throws Exception {
        when(readerService.findById(1)).thenReturn(reader);
        when(readerViewMapper.toResponse(reader)).thenReturn(readerResponse);

        mockMvc.perform(get("/readers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Иванов Иван Иванович"));
    }
    @Test
    void create_reader() throws Exception {
        when(readerDTOMapper.toEntity(any(ReaderDTO.class))).thenReturn(reader);
        when(readerService.save(any(Reader.class))).thenReturn(reader);
        when(readerViewMapper.toResponse(reader)).thenReturn(readerResponse);

        mockMvc.perform(post("/readers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"fullName\":\"Иванов Иван Иванович\","
                                + "\"dateOfBirth\":\"01.01.1990\","
                                + "\"email\":\"ivan@example.com\""
                                + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Иванов Иван Иванович"));
    }

    @Test
    void update_reader() throws Exception {
        when(readerDTOMapper.toEntity(any(ReaderDTO.class))).thenReturn(reader);
        when(readerService.update(eq(1), any(Reader.class))).thenReturn(reader);
        when(readerViewMapper.toResponse(reader)).thenReturn(readerResponse);

        mockMvc.perform(put("/readers/1/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"fullName\":\"Иванов Иван Иванович\","
                                + "\"dateOfBirth\":\"01.01.1990\","
                                + "\"email\":\"ivan@example.com\""
                                + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Иванов Иван Иванович"));
    }


    @Test
    void delete_reader() throws Exception {
        when(readerService.findById(1)).thenReturn(reader);
        when(readerViewMapper.toResponse(reader)).thenReturn(readerResponse);

        mockMvc.perform(delete("/readers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Иванов Иван Иванович"));
    }
}