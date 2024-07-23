package com.library.librarymanagementsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.library.librarymanagementsystem.service.BookDataLoaderService;

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private BookDataLoaderService bookDataLoaderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadBooks_Success() throws Exception {
        doNothing().when(bookDataLoaderService).loadBooksFromCsv();

        ResponseEntity<String> response = adminController.loadBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book data loaded successfully", response.getBody());
        verify(bookDataLoaderService, times(1)).loadBooksFromCsv();
    }

    @Test
    void testLoadBooks_Failure() throws Exception {
        String errorMessage = "CSV file not found";
        doThrow(new RuntimeException(errorMessage)).when(bookDataLoaderService).loadBooksFromCsv();

        ResponseEntity<String> response = adminController.loadBooks();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error loading book data: " + errorMessage, response.getBody());
        verify(bookDataLoaderService, times(1)).loadBooksFromCsv();
    }
}