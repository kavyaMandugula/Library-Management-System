package com.library.librarymanagementsystem.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.library.librarymanagementsystem.entities.Book;
import com.library.librarymanagementsystem.entities.Loan;
import com.library.librarymanagementsystem.model.BookDTO;
import com.library.librarymanagementsystem.repository.BookRepository;
import com.library.librarymanagementsystem.service.BookService;
import com.library.librarymanagementsystem.service.LoanService;

class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

    @Test
    void testGetBookById_Found() {
        Long bookId = 1L;
        Book book = new Book();
        when(bookService.getBookById(bookId)).thenReturn(Optional.of(book));

        ResponseEntity<Book> response = bookController.getBookById(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    void testGetBookById_NotFound() {
        Long bookId = 1L;
        when(bookService.getBookById(bookId)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookController.getBookById(bookId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddBook() {
        Book book = new Book();
        when(bookService.saveBook(book)).thenReturn(book);

        ResponseEntity<Book> response = bookController.addBook(book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    void testUpdateBook_Found() {
        Long bookId = 1L;
        Book book = new Book();
        when(bookService.updateBook(bookId, book)).thenReturn(Optional.of(book));

        ResponseEntity<Book> response = bookController.updateBook(bookId, book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    void testUpdateBook_NotFound() {
        Long bookId = 1L;
        Book book = new Book();
        when(bookService.updateBook(bookId, book)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookController.updateBook(bookId, book);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteBook_Success() {
        Long bookId = 1L;
        when(bookService.deleteBook(bookId)).thenReturn(true);

        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteBook_NotFound() {
        Long bookId = 1L;
        when(bookService.deleteBook(bookId)).thenReturn(false);

        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSearchBooks() {
        String query = "Java";
        List<BookDTO> books = Arrays.asList(new BookDTO(), new BookDTO());
        when(bookService.searchBooks(query)).thenReturn(books);

        ResponseEntity<List<BookDTO>> response = bookController.searchBooks(query);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

    @Test
    void testGetAdvancedSearchOptions() {
        when(bookRepository.findDistinctIsbns()).thenReturn(Arrays.asList("ISBN1", "ISBN2"));
        when(bookRepository.findDistinctAuthors()).thenReturn(Arrays.asList("Author1", "Author2"));
        when(bookRepository.findDistinctTitles()).thenReturn(Arrays.asList("Title1", "Title2"));

        ResponseEntity<Map<String, List<String>>> response = bookController.getAdvancedSearchOptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("isbns"));
        assertTrue(response.getBody().containsKey("authors"));
        assertTrue(response.getBody().containsKey("titles"));
        assertTrue(response.getBody().containsKey("statuses"));
    }

    @Test
    void testAdvancedSearch() {
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("author", "John Doe");
        List<BookDTO> books = Arrays.asList(new BookDTO(), new BookDTO());
        when(bookService.advancedSearch(searchCriteria)).thenReturn(books);

        ResponseEntity<List<BookDTO>> response = bookController.advancedSearch(searchCriteria);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

    @Test
    void testLoanBook_Success() throws Exception {
        Long bookId = 1L;
        String username = "user1";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);
        Loan loan = new Loan();
        when(loanService.loanBook(bookId, username)).thenReturn(loan);

        ResponseEntity<?> response = bookController.loanBook(bookId, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loan, response.getBody());
    }

    @Test
    void testLoanBook_Failure() throws Exception {
        Long bookId = 1L;
        String username = "user1";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);
        when(loanService.loanBook(bookId, username)).thenThrow(new RuntimeException("Book not available"));

        ResponseEntity<?> response = bookController.loanBook(bookId, principal);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book not available", response.getBody());
    }
}