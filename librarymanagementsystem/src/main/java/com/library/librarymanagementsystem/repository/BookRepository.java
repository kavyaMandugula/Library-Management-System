package com.library.librarymanagementsystem.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.library.librarymanagementsystem.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);
    List<Book> findByTitleContainingOrAuthorContaining(String title, String author);
    List<Book> findByTitleContainingOrAuthorContainingOrIsbnContaining(String title, String author, String isbn);

    @Query("SELECT DISTINCT b.isbn FROM Book b")
    List<String> findDistinctIsbns();

    @Query("SELECT DISTINCT b.author FROM Book b")
    List<String> findDistinctAuthors();

    @Query("SELECT DISTINCT b.title FROM Book b")
    List<String> findDistinctTitles();

    @Query("SELECT DISTINCT b.status FROM Book b")
    List<String> findDistinctStatuses();

    @Query("SELECT b FROM Book b WHERE " +
       "(:isbn IS NULL OR :isbn = '' OR b.isbn LIKE %:isbn%) AND " +
       "(:author IS NULL OR :author = '' OR b.author LIKE %:author%) AND " +
       "(:title IS NULL OR :title = '' OR b.title LIKE %:title%) AND " +
       "(:status IS NULL OR b.status = :status)")
List<Book> advancedSearch(
    @Param("isbn") String isbn,
    @Param("author") String author,
    @Param("title") String title,
    @Param("status") Book.BookStatus status
);
    
}
