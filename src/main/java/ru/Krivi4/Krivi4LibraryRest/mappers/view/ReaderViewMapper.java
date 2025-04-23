package ru.Krivi4.Krivi4LibraryRest.mappers.view;

import org.mapstruct.Mapper;
import ru.Krivi4.Krivi4LibraryRest.models.Reader;
import ru.Krivi4.Krivi4LibraryRest.views.ReaderResponse;

@Mapper(componentModel = "spring")
public interface ReaderViewMapper {
    ReaderResponse toResponse(Reader reader);
}