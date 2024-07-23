package com.library.librarymanagementsystem.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.library.librarymanagementsystem.entities.User;
import com.library.librarymanagementsystem.entities.User.UserRole;
import com.library.librarymanagementsystem.model.LoanDTO;
import com.library.librarymanagementsystem.service.LoanService;
import com.library.librarymanagementsystem.service.UserService;

class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private LoanService loanService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAccountPage_Unauthenticated() {
        String result = accountController.getAccountPage(model, null);
        assertEquals("redirect:/login", result);
    }

    @Test
    void testGetAccountPage_AuthenticatedAdmin() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("admin");

        User adminUser = new User();
        adminUser.setRole(UserRole.ADMIN);
        when(userService.getUserByUsername("admin")).thenReturn(adminUser);

        String result = accountController.getAccountPage(model, principal);
        assertEquals("redirect:/account/admin", result);
    }

    @Test
    void testGetAccountPage_AuthenticatedPatron() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("patron");

        User patronUser = new User();
        patronUser.setRole(UserRole.PATRON);
        when(userService.getUserByUsername("patron")).thenReturn(patronUser);

        String result = accountController.getAccountPage(model, principal);
        assertEquals("redirect:/account/patron", result);
    }

    @Test
    void testGetAdminAccountPage() {
        List<LoanDTO> mockLoans = Arrays.asList(new LoanDTO(), new LoanDTO());
        when(loanService.getAllActiveLoans()).thenReturn(mockLoans);

        String result = accountController.getAdminAccountPage(model);

        assertEquals("admin", result);
        verify(model).addAttribute("activeLoans", mockLoans);
    }

    @Test
    void testGetPatronAccountPage() {
        String result = accountController.getPatronAccountPage();
        assertEquals("patron", result);
    }
}