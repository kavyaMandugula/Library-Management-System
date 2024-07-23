package com.library.librarymanagementsystem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.librarymanagementsystem.entities.Book;
import com.library.librarymanagementsystem.model.BookDTO;
import com.library.librarymanagementsystem.model.BookModel;
import com.library.librarymanagementsystem.repository.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setIsbn(bookDetails.getIsbn());
                    book.setDescription(bookDetails.getDescription());
                    book.setPublishedYear(bookDetails.getPublishedYear());
                    book.setGenre(bookDetails.getGenre());
                    book.setAverageRating(bookDetails.getAverageRating());
                    book.setNumPages(bookDetails.getNumPages());
                    book.setThumbnail(bookDetails.getThumbnail());
                    book.setQuantity(bookDetails.getQuantity());
                    book.setAvailableQuantity(bookDetails.getAvailableQuantity());
                    book.setStatus(bookDetails.getStatus());
                    return bookRepository.save(book);
                });
    }

    public boolean deleteBook(Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return true;
                })
                .orElse(false);
    }

   public List<BookDTO> searchBooks(String query) {
        List<Book> books = bookRepository.findByTitleContainingOrAuthorContainingOrIsbnContaining(query, query, query);
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }



    private BookModel convertToModel(Book book) {
        BookModel searchedBook = new BookModel();
        searchedBook.setId(book.getId());
        searchedBook.setTitle(book.getTitle());
        searchedBook.setAuthor(book.getAuthor());
        searchedBook.setIsbn(book.getIsbn());
        searchedBook.setDescription(book.getDescription());
        searchedBook.setPublishedYear(book.getPublishedYear());
        searchedBook.setGenre(book.getGenre());
        searchedBook.setAverageRating(book.getAverageRating());
        searchedBook.setNumPages(book.getNumPages());
        searchedBook.setThumbnail(book.getThumbnail());
        searchedBook.setStatus(book.getStatus());
        searchedBook.setQuantity(book.getQuantity());
        searchedBook.setAvailableQuantity(book.getAvailableQuantity());
        return searchedBook;
    }

    private BookDTO convertToDTO(Book book) {
    BookDTO dto = new BookDTO();
    dto.setId(book.getId());
    dto.setTitle(book.getTitle());
    dto.setAuthor(book.getAuthor());
    dto.setIsbn(book.getIsbn());
    dto.setDescription(book.getDescription());
    dto.setPublishedYear(book.getPublishedYear());
    dto.setGenre(book.getGenre());
    Float averageRating = book.getAverageRating();
    dto.setAverageRating(averageRating != null ? Double.valueOf(averageRating) : null);
    dto.setNumPages(book.getNumPages());
    dto.setThumbnail(book.getThumbnail());
    dto.setQuantity(book.getQuantity());
    dto.setAvailableQuantity(book.getAvailableQuantity());
    dto.setStatus(book.getStatus());
    return dto;
}

public Map<String, List<String>> getAdvancedSearchOptions() {
        Map<String, List<String>> options = new HashMap<>();
        options.put("isbns", bookRepository.findDistinctIsbns());
        options.put("authors", bookRepository.findDistinctAuthors());
        options.put("titles", bookRepository.findDistinctTitles());
        options.put("statuses", bookRepository.findDistinctStatuses());
        return options;
    }

    public List<BookDTO> advancedSearch(Map<String, String> searchCriteria) {
        String isbn = StringUtils.isBlank(searchCriteria.get("isbn")) ? null : searchCriteria.get("isbn");
        String author = StringUtils.isBlank(searchCriteria.get("author")) ? null : searchCriteria.get("author");
        String title = StringUtils.isBlank(searchCriteria.get("title")) ? null : searchCriteria.get("title");
        Book.BookStatus status = null;
        if (StringUtils.isNotBlank(searchCriteria.get("status"))) {
            try {
                status = Book.BookStatus.valueOf(searchCriteria.get("status"));
            } catch (IllegalArgumentException e) {
                // Handle invalid status
                throw new IllegalArgumentException("Invalid status: " + searchCriteria.get("status"));
            }
        }
    
        List<Book> books = bookRepository.advancedSearch(isbn, author, title, status);
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    
    }
}
