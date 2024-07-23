package com.library.librarymanagementsystem.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.librarymanagementsystem.entities.Book;
import com.library.librarymanagementsystem.repository.BookRepository;
@Service
public class BookDataLoaderService  {

    private static final String CSV_FILE_PATH = "/Users/sivakasina/SoftwareEngineering/Library-Management-System/librarymanagementsystem/src/main/java/com/library/librarymanagementsystem/repository/data_load.csv";

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public void loadBooksFromCsv() {
        List<Book> books = new ArrayList<>();
        String line;
        int count = 0;
        int skipped = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;  // Skip empty lines
                }

                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // Assuming tab-separated values

                if (values.length < 12) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }

                try {
                    Book book = new Book();

                    String isbn = values[0].trim();
                    String title = values[2].trim();
                    String author = values[4].trim();
                    if(!values[8].isBlank()  && values[8].length() > 0)
                    {
                    String publishedYearstr = values[8].trim();
                    book.setPublishedYear(Integer.valueOf(publishedYearstr));
                    }
                    String genre = values[5].trim();
                    int quantity = 1;
                    int availableQuantity = 1;
                    float averageRating = values[9] != null ? Float.parseFloat(values[9].trim()) : 3.55F;
                    int numPages = values[10] != null ? Integer.parseInt(values[10].trim()) : 100;
                    String thumbnail = values[6].trim();
                    String description = values[7].trim();

                    book.setIsbn(isbn);
                    if(author.length() > 255 || title.length() > 255)
                    {
                        continue;
                    }
                    book.setTitle(title);
                
                    
                    book.setAuthor(author);
                    book.setGenre(genre);
                    book.setThumbnail(thumbnail);
                    book.setDescription(description);
                    book.setAverageRating(averageRating);
                    book.setNumPages(numPages);
                    book.setQuantity(quantity);
                    book.setAvailableQuantity(availableQuantity);
                    book.setStatus(Book.BookStatus.AVAILABLE);

                    books.add(book);
                    count++;

                    if (count % 100 == 0) {
                        bookRepository.saveAll(books);
                        books.clear();
                        System.out.println("Inserted " + count + " records");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping line due to number format error: " + line);
                    e.printStackTrace();
                }
            }

            // Insert any remaining books
            if (!books.isEmpty()) {
                bookRepository.saveAll(books);
            }

            System.out.println("Data inserted successfully. Total records: " + count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
