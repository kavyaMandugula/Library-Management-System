package com.library.librarymanagementsystem.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.library.librarymanagementsystem.entities.Book;
import com.library.librarymanagementsystem.entities.Loan;
import com.library.librarymanagementsystem.entities.User;
import com.library.librarymanagementsystem.model.LoanDTO;
import com.library.librarymanagementsystem.repository.BookRepository;
import com.library.librarymanagementsystem.repository.LoanRepository;
import com.library.librarymanagementsystem.repository.UserRepository;

class LoanServiceTest {

    @InjectMocks
    private LoanService loanService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckoutBook_Success() {
        Long userId = 1L;
        Long bookId = 1L;
        User user = new User();
        Book book = new Book();
        book.setAvailableQuantity(1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArguments()[0]);

        Loan result = loanService.checkoutBook(userId, bookId);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(book, result.getBook());
        assertEquals(Loan.LoanStatus.ACTIVE, result.getStatus());
        assertEquals(0, book.getAvailableQuantity());
    }

    @Test
    void testReturnBook_Success() {
        Long loanId = 1L;
        Loan loan = new Loan();
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        Book book = new Book();
        book.setAvailableQuantity(0);
        loan.setBook(book);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArguments()[0]);
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        Loan result = loanService.returnBook(loanId);

        assertNotNull(result);
        assertEquals(Loan.LoanStatus.RETURNED, result.getStatus());
        assertNotNull(result.getReturnDate());
        assertEquals(1, book.getAvailableQuantity());
    }

    @Test
    void testGetActiveLoansForUser() {
        Long userId = 1L;
        List<Loan> expectedLoans = Arrays.asList(new Loan(), new Loan());

        when(loanRepository.findByUserIdAndStatus(userId, Loan.LoanStatus.ACTIVE)).thenReturn(expectedLoans);

        List<Loan> result = loanService.getActiveLoansForUser(userId);

        assertEquals(expectedLoans, result);
    }

    @Test
    void testGetOverdueLoans() {
        List<Loan> expectedLoans = Arrays.asList(new Loan(), new Loan());

        when(loanRepository.findByStatusAndDueDateBefore(eq(Loan.LoanStatus.ACTIVE), any(LocalDateTime.class)))
                .thenReturn(expectedLoans);

        List<Loan> result = loanService.getOverdueLoans();

        assertEquals(expectedLoans, result);
    }

    @Test
    void testGetLoansByUserId() {
        Long userId = 1L;
        List<Loan> loans = Arrays.asList(createSampleLoan(), createSampleLoan());

        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        List<LoanDTO> result = loanService.getLoansByUserId(userId);

        assertEquals(2, result.size());
        // Add more assertions to verify the content of LoanDTOs
    }

    @Test
    void testGetAllActiveLoans() {
        List<Loan> activeLoans = Arrays.asList(createSampleLoan(), createSampleLoan());

        when(loanRepository.findByStatus(Loan.LoanStatus.ACTIVE)).thenReturn(activeLoans);

        List<LoanDTO> result = loanService.getAllActiveLoans();

        assertEquals(2, result.size());
        // Add more assertions to verify the content of LoanDTOs
    }

    @Test
    void testGetAllActiveLoansFromLoans() {
        List<Loan> activeLoans = Arrays.asList(createSampleLoan(), createSampleLoan());

        when(loanRepository.findByStatus(Loan.LoanStatus.ACTIVE)).thenReturn(activeLoans);

        List<Loan> result = loanService.getAllActiveLoansFromLoans();

        assertEquals(2, result.size());
        // Add more assertions if needed
    }

    @Test
    void testLoanBook_Success() {
        Long bookId = 1L;
        String username = "testuser";
        Book book = new Book();
        book.setAvailableQuantity(1);
        User user = new User();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArguments()[0]);

        Loan result = loanService.loanBook(bookId, username);

        assertNotNull(result);
        assertEquals(book, result.getBook());
        assertEquals(user, result.getUser());
        assertEquals(Loan.LoanStatus.ACTIVE, result.getStatus());
        assertEquals(0, book.getAvailableQuantity());
    }

    @Test
    void testGetUserLoans() {
        Long userId = 1L;
        List<Loan> loans = Arrays.asList(createSampleLoan(), createSampleLoan());

        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        List<LoanDTO> result = loanService.getUserLoans(userId);

        assertEquals(2, result.size());
        // Add more assertions to verify the content of LoanDTOs
    }

    @Test
    void testAddLoan() {
        String bookId = "1";
        String userId = "1";
        User user = new User();
        Book book = new Book();
        book.setAvailableQuantity(1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> i.getArguments()[0]);

        assertDoesNotThrow(() -> loanService.addLoan(bookId, userId));
    }

    @Test
    void testRemoveLoan() {
        Long loanId = 1L;

        assertDoesNotThrow(() -> loanService.removeLoan(loanId));
        verify(loanRepository).deleteById(loanId);
    }

    @Test
void testRemoveBookFromUser() {
    Long userId = 1L;
    Long bookId = 1L;
    User user = new User();
    Book book = new Book();
    book.setId(bookId);
    Loan loan = new Loan();
    loan.setBook(book);
    
    Set<Loan> loans = new HashSet<>();
    loans.add(loan);
    user.setLoans(loans);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(bookService.getBookById(bookId)).thenReturn(Optional.of(book));
    when(userRepository.save(any(User.class))).thenReturn(user);

    loanService.removeBookFromUser(userId, bookId);

    assertTrue(user.getLoans().isEmpty());
    verify(userRepository).save(user);
}

    private Loan createSampleLoan() {
        Loan loan = new Loan();
        Book book = new Book();
        book.setTitle("Sample Book");
        book.setAuthor("Sample Author");
        loan.setBook(book);
        loan.setCheckoutDate(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusDays(14));
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        return loan;
    }
}