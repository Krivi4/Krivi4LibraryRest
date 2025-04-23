package ru.Krivi4.Krivi4LibraryRest.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksRestController {

    private final BookService booksService;
    private final BookDTOMapper bookDTOMapper;
    private final ReaderService readerService;
    private final BookViewMapper bookViewMapper;
    private final ReaderViewMapper readerViewMapper;


    //Read//

    @GetMapping()
    public List<BookResponse> getBooks(Pageable pageable){
        return booksService
                .findAll(pageable)
                .map(bookViewMapper::toResponse)
                .getContent();
    }

    @GetMapping("/{id}")
    public BookResponse getBook(@PathVariable("id") int id){
        return bookViewMapper.toResponse((booksService.findById(id)));
    }

    @GetMapping("/{id}/owner")
    public ReaderResponse getBookOwner(@PathVariable("id") int id){
        return readerViewMapper.toResponse(booksService.getBookOwner(id));
    }

    //Search
    @GetMapping("/search")
    public List<BookResponse> search(@RequestParam("query") String query) {
        return booksService
                .searchByTitle(query)
                .stream()
                .map(bookViewMapper::toResponse)
                .collect(Collectors.toList());
    }

    //Create//
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody @Valid BookDTO bookDTO) {

        Book toSave = bookDTOMapper.toEntity(bookDTO);
        Book saved  = booksService.save(toSave);
        BookResponse resp = bookViewMapper.toResponse(saved);

        Map<String,Object> body = new LinkedHashMap<>();
        body.put("message",     "Книга с данными ниже добавлена");
        body.put("title",    resp.getTitle());
        body.put("author", resp.getAuthor());
        body.put("year",    resp.getYear());


        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }



//Update//
@PutMapping("/{id}/edit")
public ResponseEntity<Map<String, Object>> update(@PathVariable("id") int id, @RequestBody @Valid BookDTO bookDTO) {

    Book toUpdate = bookDTOMapper.toEntity(bookDTO);
    Book updated  = booksService.update(id, toUpdate);
    BookResponse resp = bookViewMapper.toResponse(updated);

    Map<String,Object> body = new LinkedHashMap<>();
    body.put("message",     "Книга с данными ниже обновлена");
    body.put("title",    resp.getTitle());
    body.put("author", resp.getAuthor());
    body.put("year",    resp.getYear());


    return ResponseEntity
            .status(HttpStatus.OK)
            .body(body);
}


    //Delete//
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable("id") int id){
        Book b = booksService.findById(id);
        BookResponse resp = bookViewMapper.toResponse(b);

        booksService.delete(id);

        Map<String,Object> body = new LinkedHashMap<>();
        body.put("message",     "Книга с данными ниже удалена");
        body.put("title",    resp.getTitle());
        body.put("author", resp.getAuthor());
        body.put("year",    resp.getYear());

        return ResponseEntity.ok(body);
    }


    //Appoint//
    @PutMapping("/{id}/appoint")
    public ResponseEntity<Map<String, Object>> appoint(@PathVariable("id") int id, @RequestParam("readerId") int readerId) {

        Book b = booksService.findById(id);
        BookResponse bookResp = bookViewMapper.toResponse(b);

        Reader reader = readerService.findById(readerId);
        ReaderResponse readerResp = readerViewMapper.toResponse(reader);
        booksService.appoint(id, reader);

        Map<String,Object> body = new LinkedHashMap<>();
        body.put("messageBook",     "Книга с данными ");
        body.put("title",    bookResp.getTitle());
        body.put("author", bookResp.getAuthor());
        body.put("year",    bookResp.getYear());
        body.put("messageReader",     "назначена пользователю с данными  ");
        body.put("fullName",    readerResp.getFullName());
        body.put("dateOfBirth", readerResp.getDateOfBirth());
        body.put("email",       readerResp.getEmail());

        return ResponseEntity.ok(body);
    }

    //Release//
    @PutMapping("/{id}/release")
    public ResponseEntity<Map<String, Object>> release(@PathVariable("id") int id) {

        Book b = booksService.findById(id);
        BookResponse bookResp = bookViewMapper.toResponse(b);

        booksService.release(id);

        Map<String,Object> body = new LinkedHashMap<>();
        body.put("messageBook",     "Книга с данными ниже освобождена ");
        body.put("title",    bookResp.getTitle());
        body.put("author", bookResp.getAuthor());
        body.put("year",    bookResp.getYear());

        return ResponseEntity.ok(body);
    }


}
