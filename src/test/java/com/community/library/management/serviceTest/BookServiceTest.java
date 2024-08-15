package com.community.library.management.serviceTest;

import com.community.library.management.dto.BookDTO;
import com.community.library.management.exception.BookNotDeletableException;
import com.community.library.management.exception.BookNotFoundException;
import com.community.library.management.model.Book;
import com.community.library.management.repository.BookRepository;
import com.community.library.management.repository.BorrowedBookRepository;
import com.community.library.management.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowedBookRepository borrowedBookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    private static Stream<Arguments> provideBooksForSaving() {
        return Stream.of(
                Arguments.of("New Title", "New Author", 10, 10, true),
                Arguments.of("Existing Title", "Existing Author", 5, 15, false),
                Arguments.of("Another Title", "Another Author", 0, 11, false)
        );
    }
    @ParameterizedTest
    @MethodSource("provideBooksForSaving")
    public void testSaveBook(String title, String author, int amount, int expectedAmount, boolean isNewBook) {
        Book book = new Book(title, author, amount);

        if (isNewBook) {
            when(bookRepository.findByTitleAndAuthor(title, author)).thenReturn(Optional.empty());
            when(bookRepository.save(book)).thenReturn(book);
        } else {
            Book existingBook = new Book(title, author, 10);
            when(bookRepository.findByTitleAndAuthor(title, author)).thenReturn(Optional.of(existingBook));
            when(bookRepository.save(existingBook)).thenReturn(existingBook);
        }

        Book savedBook = bookService.save(book);

        assertNotNull(savedBook);
        assertEquals(expectedAmount, savedBook.getAmount());

        if (isNewBook) {
            verify(bookRepository, times(1)).save(book);
        } else {
            verify(bookRepository, times(1)).save(any(Book.class));
        }
    }
    @Test
    void testFindAll() {
        List<Book> books = Arrays.asList(new Book("Title1", "Author1", 1),
                new Book("Title2", "Author2", 2));
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.findAll();

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }
    @Test
    void testFindByIdSuccess() {
        Book book = new Book("Test Title", "Test Author", 1);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Title", result.get().getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }
    @Test
    void testFindByIdNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.findById(1L);

        assertFalse(result.isPresent());
        verify(bookRepository, times(1)).findById(1L);
    }
    @Test
    public void testSaveNewBook() {
        Book book = new Book("New Title", "New Author", 1);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book savedBook = bookService.save(book);

        assertEquals(book.getTitle(), savedBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testSaveExistingBook() {
        Book existingBook = new Book("Existing Title", "Existing Author", 5);
        when(bookRepository.findByTitleAndAuthor("Existing Title", "Existing Author"))
                .thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book updatedBook = bookService.save(new Book("Existing Title", "Existing Author", 1));

        assertEquals(6, updatedBook.getAmount());
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    public void testDeleteBookSuccess() {
        Book book = new Book("Test Title", "Test Author", 0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowedBookRepository.existsByBookId(1L)).thenReturn(false);

        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    public void testDeleteBookBookIsBorrowed() {
        Book book = new Book("Test Title", "Test Author", 1);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowedBookRepository.existsByBookId(1L)).thenReturn(true);

        BookNotDeletableException exception = assertThrows(BookNotDeletableException.class, () -> {
            bookService.deleteBook(1L);
        });

        assertEquals("Book cannot be deleted because it is borrowed", exception.getMessage());
        verify(bookRepository, never()).deleteById(1L);
    }
    @Test
    public void testUpdateBookSuccess() {
        Book book = new Book("Old Title", "Old Author", 10);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDTO bookDTO = new BookDTO("New Title", "New Author", 5);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book updatedBook = bookService.updateBook(1L, bookDTO);

        assertEquals("New Title", updatedBook.getTitle());
        assertEquals("New Author", updatedBook.getAuthor());
        assertEquals(5, updatedBook.getAmount());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testUpdateBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BookDTO bookDTO = new BookDTO("New Title", "New Author", 5);
        assertThrows(BookNotFoundException.class, () -> {
            bookService.updateBook(1L, bookDTO);
        });

        verify(bookRepository, never()).save(any(Book.class));
    }
    @Test
    void testDeleteBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1L));
    }
}
