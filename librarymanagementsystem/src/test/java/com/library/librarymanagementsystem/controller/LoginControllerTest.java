package com.library.librarymanagementsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;

import com.library.librarymanagementsystem.service.UserService;

import jakarta.servlet.http.HttpSession;

public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession httpSession;

    @Mock
    private Model model;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIndex() {
        assertEquals("index", loginController.index());
    }

    @Test
    public void testLoginPage() {
        assertEquals("login", loginController.loginPage());
    }

    @Test
    public void testLoginSuccess() {
        String username = "testuser";
        String password = "testpass";
        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        String result = loginController.login(username, password, httpSession);

        assertEquals("redirect:/dashboard", result);
        verify(httpSession).setAttribute(eq("SPRING_SECURITY_CONTEXT"), any());
    }

    @Test
    public void testLoginFailure() {
        String username = "testuser";
        String password = "wrongpass";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new AuthenticationException("Authentication failed") {});

        String result = loginController.login(username, password, httpSession);

        assertEquals("redirect:/login?error", result);
    }

    @Test
    public void testLogout() {
        String result = loginController.logout(httpSession);

        assertEquals("redirect:/login?logout", result);
        verify(httpSession).invalidate();
    }

    @Test
    public void testDashboardWithUnauthenticatedUser() {
        String result = loginController.dashboard(null, model);

        assertEquals("redirect:/login", result);
    }
}