package com.community.library.management.service;

import com.community.library.management.exception.MemberNotFoundException;
import com.community.library.management.model.Book;
import com.community.library.management.model.BorrowedBook;
import com.community.library.management.model.Member;
import com.community.library.management.repository.BookRepository;
import com.community.library.management.repository.BorrowedBookRepository;
import com.community.library.management.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BorrowingService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    @Value("${library.member.max-borrow-limit}")
    private int maxBorrowLimit;

    public void borrowBook(Long memberId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        if (book.getAmount() <= 0) {
            throw new IllegalArgumentException("Book is not available for borrowing");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (member.getBorrowedBooks() == null) {
            member.setBorrowedBooks(new ArrayList<>());
        }


        if (member.getBorrowedBooks().size() >= maxBorrowLimit) {
            throw new IllegalArgumentException("Member has reached the borrow limit");
        }

        BorrowedBook borrowedBook = new BorrowedBook(book, member);
        member.getBorrowedBooks().add(borrowedBook);

        book.setAmount(book.getAmount() - 1);
        bookRepository.save(book);
        borrowedBookRepository.save(borrowedBook);
    }

    public void returnBook(Long memberId, Long bookId) {
        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByMemberIdAndBookId(memberId, bookId);

        if (borrowedBooks.isEmpty()) {
            throw new IllegalArgumentException("No borrowed book found for this member and book");
        }

        BorrowedBook borrowedBook = borrowedBooks.get(0);

        Book book = borrowedBook.getBook();
        book.setAmount(book.getAmount() + 1);
        bookRepository.save(book);

        borrowedBookRepository.delete(borrowedBook);
    }

    public List<BorrowedBook> getBooksBorrowedByMemberName(String memberName) {
        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        return borrowedBookRepository.findByMemberName(member.getName());
    }

    public List<String> getDistinctBorrowedBookNames() {
        return borrowedBookRepository.findDistinctBorrowedBookTitles();
    }

    public List<Object[]> getDistinctBorrowedBookNamesWithCount() {
        return borrowedBookRepository.findDistinctBorrowedBookTitlesAndCount();
    }
}