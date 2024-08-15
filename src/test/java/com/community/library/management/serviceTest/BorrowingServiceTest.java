package com.community.library.management.serviceTest;

import com.community.library.management.model.Book;
import com.community.library.management.model.BorrowedBook;
import com.community.library.management.model.Member;
import com.community.library.management.repository.BookRepository;
import com.community.library.management.repository.BorrowedBookRepository;
import com.community.library.management.repository.MemberRepository;
import com.community.library.management.service.BorrowingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
        "library.member.max-borrow-limit=10"
})
public class BorrowingServiceTest {

    @Autowired
    private BorrowingService borrowingService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private BorrowedBookRepository borrowedBookRepository;
    @Value("${library.member.max-borrow-limit}")
    private int maxBorrowLimit;

    private static Stream<Arguments> provideBorrowBookTestCases() {
        return Stream.of(
                Arguments.of(new Book("Test Book", "Test Author", 3), createMemberWithBorrowedBooks(0), 2, null),
                Arguments.of(null, createMemberWithBorrowedBooks(0), 0, "Book not found"),
                Arguments.of(new Book("Test Book", "Test Author", 0), createMemberWithBorrowedBooks(0), 0, "Book is not available for borrowing"),
                Arguments.of(new Book("Test Book", "Test Author", 5), createMemberWithBorrowedBooks(10), 0, "Member has reached the borrow limit")
        );
    }


    private static Member createMemberWithBorrowedBooks(int borrowedBooksCount) {
        Member member = new Member("Test Member");
        for (int i = 0; i < borrowedBooksCount; i++) {
            member.getBorrowedBooks().add(new BorrowedBook());
        }
        return member;
    }
    @ParameterizedTest
    @MethodSource("provideBorrowBookTestCases")
    public void testBorrowBook(Book book, Member member, int expectedBookAmount, String expectedExceptionMessage) {
        if (book != null) {
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        } else {
            when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        }

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        if (expectedExceptionMessage == null) {
            borrowingService.borrowBook(1L, 1L);
            assert book != null;
            assertEquals(expectedBookAmount, book.getAmount());
            verify(bookRepository, times(1)).save(book);
            verify(borrowedBookRepository, times(1)).save(any(BorrowedBook.class));
        } else {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                borrowingService.borrowBook(1L, 1L);
            });

            assertEquals(expectedExceptionMessage, exception.getMessage());
        }
    }

    @Test
    public void testBorrowBookSuccess() {
        Member member = new Member("Test Member");
        member.setId(1L);

        member.getBorrowedBooks().clear();

        Book book = new Book("Test Title", "Test Author", 5);
        book.setId(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowedBookRepository.save(any(BorrowedBook.class))).thenReturn(new BorrowedBook(book, member));

        borrowingService.borrowBook(1L, 1L);

        assertEquals(4, book.getAmount());
        verify(bookRepository, times(1)).save(book);
        verify(borrowedBookRepository, times(1)).save(any(BorrowedBook.class));
    }
    @Test
    public void testBorrowBookBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            borrowingService.borrowBook(1L, 1L);
        });

        assertEquals("Book not found", exception.getMessage());
    }
    @Test
    public void testBorrowBookMemberNotFound() {
        Book book = new Book("Test Book", "Test Author", 3);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            borrowingService.borrowBook(1L, 1L);
        });

        assertEquals("Member not found", exception.getMessage());
    }

    @Test
    public void testBorrowBookMemberReachedBorrowLimit() {
        Book book = new Book("Test Book", "Test Author", 3);
        Member member = new Member("Test Member");
        member.setBorrowedBooks(new ArrayList<>(Collections.nCopies(maxBorrowLimit, new BorrowedBook())));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            borrowingService.borrowBook(1L, 1L);
        });

        assertEquals("Member has reached the borrow limit", exception.getMessage());
    }
    @Test
    public void testBorrowBookNoAvailableCopies() {
        Member member = new Member("Test Member");
        member.setId(1L);

        member.getBorrowedBooks().clear();

        Book book = new Book("Test Title", "Test Author", 0);
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            borrowingService.borrowBook(1L, 1L);
        });

        assertEquals("Book is not available for borrowing", exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
        verify(borrowedBookRepository, never()).save(any(BorrowedBook.class));
    }
}