package com.community.library.management.controller;

import com.community.library.management.dto.BookDTO;
import com.community.library.management.mapper.BookMapper;
import com.community.library.management.model.Book;
import com.community.library.management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {


    private final BookService bookService;
    private final BookMapper bookMapper;

    @Autowired
    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.findAll();
        List<BookDTO> bookDTOs = books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return new ResponseEntity<>(bookMapper.toDTO(book), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBook(@RequestBody @Valid BookDTO bookDTO) {
        Book book = bookService.save(bookMapper.toEntity(bookDTO));
        return new ResponseEntity<>(bookMapper.toDTO(book), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long id,
            @RequestBody @Valid BookDTO bookDTO) {
        Book updatedBook = bookService.updateBook(id, bookMapper.toEntity(bookDTO));
        return ResponseEntity.ok(bookMapper.toDTO(updatedBook));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}