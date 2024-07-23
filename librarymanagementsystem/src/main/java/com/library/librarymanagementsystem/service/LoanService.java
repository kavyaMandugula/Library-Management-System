package com.library.librarymanagementsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.librarymanagementsystem.entities.Book;
import com.library.librarymanagementsystem.entities.Loan;
import com.library.librarymanagementsystem.entities.Loan.LoanStatus;
import com.library.librarymanagementsystem.entities.User;
import com.library.librarymanagementsystem.model.LoanDTO;
import com.library.librarymanagementsystem.repository.BookRepository;
import com.library.librarymanagementsystem.repository.LoanRepository;
import com.library.librarymanagementsystem.repository.UserRepository;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public Loan checkoutBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Book is not available for checkout");
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setCheckoutDate(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusDays(14)); // Set due date to 14 days from now
        loan.setStatus(Loan.LoanStatus.ACTIVE);

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != Loan.LoanStatus.ACTIVE) {
            throw new RuntimeException("This loan is not active");
        }

        loan.setReturnDate(LocalDateTime.now());
        loan.setStatus(Loan.LoanStatus.RETURNED);

        Book book = loan.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    public List<Loan> getActiveLoansForUser(Long userId) {
        return loanRepository.findByUserIdAndStatus(userId, Loan.LoanStatus.ACTIVE);
    }

    public List<Loan> getOverdueLoans() {
        return loanRepository.findByStatusAndDueDateBefore(Loan.LoanStatus.ACTIVE, LocalDateTime.now());
    }

    public List<LoanDTO> getLoansByUserId(Long userId) {
        List<Loan> loans = loanRepository.findByUserId(userId);
        return loans.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private LoanDTO convertToDTO(Loan loan) {
        return new LoanDTO(
            loan.getId(),
            loan.getBook().getTitle(),
            loan.getBook().getAuthor(),
            loan.getCheckoutDate(),
            loan.getDueDate(),
            loan.getStatus().toString()
        );
    }

    public List<LoanDTO> getAllActiveLoans() {
        return loanRepository.findByStatus(LoanStatus.ACTIVE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    @Transactional
    public Loan loanBook(Long bookId, String username) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (book.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Book is not available for loan");
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setCheckoutDate(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusDays(14)); // 2 weeks loan period
        loan.setStatus(Loan.LoanStatus.ACTIVE);

        return loanRepository.save(loan);
    }

}
