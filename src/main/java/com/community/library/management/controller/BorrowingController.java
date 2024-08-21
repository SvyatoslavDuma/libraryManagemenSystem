package com.community.library.management.controller;

import com.community.library.management.model.BorrowedBook;
import com.community.library.management.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowingController {


    private final BorrowingService borrowingService;
    @Autowired
    public BorrowingController (BorrowingService borrowingService){
        this.borrowingService=borrowingService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<Void> borrowBook(@RequestParam Long memberId, @RequestParam Long bookId) {
        try {
            borrowingService.borrowBook(memberId, bookId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/return")
    public ResponseEntity<Void> returnBook(@RequestParam Long memberId, @RequestParam Long bookId) {
        try {
            borrowingService.returnBook(memberId, bookId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/members/{name}/borrowed-books")
    public ResponseEntity<List<BorrowedBook>> getBooksBorrowedByMemberName(@PathVariable String name) {
        List<BorrowedBook> books = borrowingService.getBooksBorrowedByMemberName(name);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/borrowed-books/distinct-names")
    public ResponseEntity<List<String>> getDistinctBorrowedBookNames() {
        List<String> bookNames = borrowingService.getDistinctBorrowedBookNames();
        return ResponseEntity.ok(bookNames);
    }

    @GetMapping("/borrowed-books/distinct-names-with-count")
    public ResponseEntity<List<Object[]>> getDistinctBorrowedBookNamesWithCount() {
        List<Object[]> bookNamesWithCount = borrowingService.getDistinctBorrowedBookNamesWithCount();
        return ResponseEntity.ok(bookNamesWithCount);
    }
}