package ru.Krivi4.Krivi4LibraryRest.mappers.dto;

import org.mapstruct.Mapper;
import ru.Krivi4.Krivi4LibraryRest.dtos.ReaderDTO;
import ru.Krivi4.Krivi4LibraryRest.models.Reader;

@Mapper(componentModel = "spring")
public interface ReaderDTOMapper {
    Reader toEntity(ReaderDTO dto);
}
