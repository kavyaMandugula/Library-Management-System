package com.library.librarymanagementsystem.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.library.librarymanagementsystem.entities.Book;
import com.library.librarymanagementsystem.model.BookDTO;
import com.library.librarymanagementsystem.repository.BookRepository;
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(books, result);
    }

    @Test
    void testGetBookById() {
        Long bookId = 1L;
        Book book = new Book();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(bookId);

        assertTrue(result.isPresent());
        assertEquals(book, result.get());
    }

    @Test
    void testSaveBook() {
        Book book = new Book();
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.saveBook(book);

        assertEquals(book, result);
    }

    @Test
    void testUpdateBook() {
        Long bookId = 1L;
        Book existingBook = new Book();
        existingBook.setId(bookId);
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<Book> result = bookService.updateBook(bookId, updatedBook);

        assertTrue(result.isPresent());
        assertEquals("Updated Title", result.get().getTitle());
    }

    @Test
    void testDeleteBook() {
        Long bookId = 1L;
        Book book = new Book();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        boolean result = bookService.deleteBook(bookId);

        assertTrue(result);
        verify(bookRepository).delete(book);
    }

    @Test
    void testSearchBooks() {
        String query = "test";
        Book book1 = new Book();
        book1.setAverageRating(4.5f);
        Book book2 = new Book();
        book2.setAverageRating(null);
        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findByTitleContainingOrAuthorContainingOrIsbnContaining(query, query, query)).thenReturn(books);

        List<BookDTO> result = bookService.searchBooks(query);

        assertEquals(books.size(), result.size());
        assertEquals(Double.valueOf(4.5), result.get(0).getAverageRating());
        assertNull(result.get(1).getAverageRating());
    }

    @Test
    void testGetAdvancedSearchOptions() {
        List<String> isbns = Arrays.asList("ISBN1", "ISBN2");
        List<String> authors = Arrays.asList("Author1", "Author2");
        List<String> titles = Arrays.asList("Title1", "Title2");
        List<String> statuses = Arrays.asList("AVAILABLE", "BORROWED");

        when(bookRepository.findDistinctIsbns()).thenReturn(isbns);
        when(bookRepository.findDistinctAuthors()).thenReturn(authors);
        when(bookRepository.findDistinctTitles()).thenReturn(titles);
        when(bookRepository.findDistinctStatuses()).thenReturn(statuses);

        Map<String, List<String>> result = bookService.getAdvancedSearchOptions();

        assertEquals(4, result.size());
        assertEquals(isbns, result.get("isbns"));
        assertEquals(authors, result.get("authors"));
        assertEquals(titles, result.get("titles"));
        assertEquals(statuses, result.get("statuses"));
    }

    @Test
    void testAdvancedSearch() {
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("isbn", "1234567890");
        searchCriteria.put("author", "John Doe");
        searchCriteria.put("title", "Test Book");
        searchCriteria.put("status", "AVAILABLE");

        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.advancedSearch(eq("1234567890"), eq("John Doe"), eq("Test Book"), eq(Book.BookStatus.AVAILABLE)))
                .thenReturn(books);

        List<BookDTO> result = bookService.advancedSearch(searchCriteria);

        assertEquals(books.size(), result.size());
    }

    @Test
    void testAdvancedSearch_InvalidStatus() {
        Map<String, String> searchCriteria = new HashMap<>();
        searchCriteria.put("status", "INVALID_STATUS");

        assertThrows(IllegalArgumentException.class, () -> bookService.advancedSearch(searchCriteria));
    }

    @Test
    void testConvertEnityBookDTOs() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        List<Book> books = Arrays.asList(book1, book2);

        List<BookDTO> result = bookService.convertEnityBookDTOs(books);

        assertEquals(books.size(), result.size());
        assertEquals(book1.getId(), result.get(0).getId());
        assertEquals(book1.getTitle(), result.get(0).getTitle());
        assertEquals(book2.getId(), result.get(1).getId());
        assertEquals(book2.getTitle(), result.get(1).getTitle());
    }
}