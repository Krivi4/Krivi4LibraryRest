package ru.Krivi4.Krivi4LibraryRest.mappers.dto;

import org.mapstruct.Mapper;
import ru.Krivi4.Krivi4LibraryRest.dtos.BookDTO;
import ru.Krivi4.Krivi4LibraryRest.models.Book;


@Mapper(componentModel = "spring")
public interface BookDTOMapper {

    Book toEntity(BookDTO dto);
    }
