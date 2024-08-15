package com.community.library.management.repository;


import com.community.library.management.model.BorrowedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {

    List<BorrowedBook> findByMemberName(String name);

    @Query("SELECT DISTINCT b.book.title FROM BorrowedBook b")
    List<String> findDistinctBorrowedBookTitles();

    @Query("SELECT b.book.title, COUNT(b.book) FROM BorrowedBook b GROUP BY b.book.title")
    List<Object[]> findDistinctBorrowedBookTitlesAndCount();

    List<BorrowedBook> findByMemberIdAndBookId(Long memberId, Long bookId);

    boolean existsByBookId(Long bookId);
}