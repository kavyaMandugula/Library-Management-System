package com.library.librarymanagementsystem.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.library.librarymanagementsystem.entities.User;
import com.library.librarymanagementsystem.model.LoanDTO;
import com.library.librarymanagementsystem.service.LoanService;
import com.library.librarymanagementsystem.service.UserService;

class LoanControllerTest {

    @InjectMocks
    private LoanController loanController;

    @Mock
    private UserService userService;

    @Mock
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserLoans_WithAuthenticatedUser() {
        // Arrange
        Principal mockPrincipal = mock(Principal.class);
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        List<LoanDTO> mockLoans = Arrays.asList(
            new LoanDTO(), new LoanDTO()
        );

        when(mockPrincipal.getName()).thenReturn("testUser");
        when(userService.getUserByUsername("testUser")).thenReturn(mockUser);
        when(loanService.getLoansByUserId(1L)).thenReturn(mockLoans);

        // Act
        ResponseEntity<List<LoanDTO>> response = loanController.getUserLoans(mockPrincipal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockLoans, response.getBody());
        verify(userService).getUserByUsername("testUser");
        verify(loanService).getLoansByUserId(1L);
    }

    @Test
    void testGetUserLoans_WithUnauthenticatedUser() {
        // Arrange
        Principal mockPrincipal = null;

        // Act
        ResponseEntity<List<LoanDTO>> response = loanController.getUserLoans(mockPrincipal);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService, never()).getUserByUsername(anyString());
        verify(loanService, never()).getLoansByUserId(anyLong());
    }
}