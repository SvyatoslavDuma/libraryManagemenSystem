package com.community.library.management.service;

import com.community.library.management.dto.BookDTO;
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

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        if (existingBook.isPresent()) {
            Book bookToUpdate = existingBook.get();
            if(book.getAmount()==0){bookToUpdate.setAmount(bookToUpdate.getAmount() + 1);
            }else{bookToUpdate.setAmount(bookToUpdate.getAmount() + book.getAmount());}
            return bookRepository.save(bookToUpdate);
        } else {
            return bookRepository.save(book);
        }
    }

    public Book updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setAmount(bookDTO.getAmount());

        return bookRepository.save(book);
    }


    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        boolean isBookBorrowed = borrowedBookRepository.existsByBookId(id);

        if (isBookBorrowed) {
            throw new BookNotDeletableException("Book cannot be deleted because it is borrowed");
        }
        bookRepository.delete(book);
    }
}