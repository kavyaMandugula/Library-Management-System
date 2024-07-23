package com.library.librarymanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.librarymanagementsystem.service.BookDataLoaderService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private BookDataLoaderService bookDataLoaderService;

    @PostMapping("/load-books")
    public ResponseEntity<String> loadBooks() {
        try {
            bookDataLoaderService.loadBooksFromCsv();
            return ResponseEntity.ok("Book data loaded successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error loading book data: " + e.getMessage());
        }
    }
}
