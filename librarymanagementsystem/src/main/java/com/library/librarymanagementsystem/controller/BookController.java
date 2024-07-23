
package com.library.librarymanagementsystem.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.librarymanagementsystem.entities.Book;
import com.library.librarymanagementsystem.entities.Loan;
import com.library.librarymanagementsystem.model.BookDTO;
import com.library.librarymanagementsystem.repository.BookRepository;
import com.library.librarymanagementsystem.service.BookService;
import com.library.librarymanagementsystem.service.LoanService;

/**
 *
 * @author kavya
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanService loanService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.saveBook(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookService.deleteBook(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam String query) {
        return ResponseEntity.ok(bookService.searchBooks(query));
    }

    @GetMapping("/advanced-search-options")
    public ResponseEntity<Map<String, List<String>>> getAdvancedSearchOptions() {
       Map<String, List<String>> options = new HashMap<>();
    options.put("isbns", bookRepository.findDistinctIsbns());
    options.put("authors", bookRepository.findDistinctAuthors());
    options.put("titles", bookRepository.findDistinctTitles());
    options.put("statuses", Arrays.stream(Book.BookStatus.values())
                                  .map(Enum::name)
                                  .collect(Collectors.toList()));
                                  return ResponseEntity.ok(options);
    }

    @PostMapping("/advanced-search")
    public ResponseEntity<List<BookDTO>> advancedSearch(@RequestBody Map<String, String> searchCriteria) {
        return ResponseEntity.ok(bookService.advancedSearch(searchCriteria));
    }

    @PostMapping("/{bookId}/loan")
    public ResponseEntity<?> loanBook(@PathVariable Long bookId, Principal principal) {
        try {
            Loan loan = loanService.loanBook(bookId, principal.getName());
            return ResponseEntity.ok(loan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
