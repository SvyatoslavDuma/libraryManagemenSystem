package com.community.library.management.service;


import com.community.library.management.exception.BookNotDeletableException;
import com.community.library.management.exception.BookNotFoundException;
import com.community.library.management.model.Book;
import com.community.library.management.repository.BookRepository;
import com.community.library.management.repository.BorrowedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BorrowedBookRepository borrowedBookRepository;

    @Autowired
    public BookService(BookRepository bookRepository, BorrowedBookRepository borrowedBookRepository) {
        this.bookRepository = bookRepository;
        this.borrowedBookRepository = borrowedBookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
    }


    public Book save(Book book) {
        Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        if (existingBook.isPresent()) {
            Book bookToUpdateAmount = existingBook.get();
            if(book.getAmount()==0){bookToUpdateAmount.setAmount(bookToUpdateAmount.getAmount() + 1);
            }else{bookToUpdateAmount.setAmount(bookToUpdateAmount.getAmount() + book.getAmount());}
            return bookRepository.save(bookToUpdateAmount);
        } else {
            return bookRepository.save(book);
        }
    }

    public Book updateBook(Long id, Book book) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setAmount(book.getAmount());

        return bookRepository.save(existingBook);
    }


    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
        boolean isBookBorrowed = borrowedBookRepository.existsByBookId(id);

        if (isBookBorrowed) {
            throw new BookNotDeletableException();
        }
        bookRepository.delete(book);
    }
}