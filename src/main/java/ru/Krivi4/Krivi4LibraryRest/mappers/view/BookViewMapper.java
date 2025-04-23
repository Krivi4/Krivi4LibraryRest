package ru.Krivi4.Krivi4LibraryRest.mappers.view;

import org.mapstruct.Mapper;
import ru.Krivi4.Krivi4LibraryRest.models.Book;
import ru.Krivi4.Krivi4LibraryRest.views.BookResponse;

@Mapper(componentModel = "spring")
public interface BookViewMapper {
    BookResponse toResponse(Book book);
}