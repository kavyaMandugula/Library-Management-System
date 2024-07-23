package com.library.librarymanagementsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.library.librarymanagementsystem.model.PasswordResetRequest;
import com.library.librarymanagementsystem.model.UserModel;
import com.library.librarymanagementsystem.model.UserUpdateDTO;
import com.library.librarymanagementsystem.service.UserService;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowRegistrationForm() {
        assertEquals("registration", userController.showRegistrationForm());
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        UserModel user = new UserModel();
        when(userService.registerUser(user)).thenReturn(user);

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testRegisterUser_Failure() throws Exception {
        UserModel user = new UserModel();
        when(userService.registerUser(user)).thenThrow(new RuntimeException("Registration failed"));

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Registration failed", response.getBody());
    }

    @Test
    void testLoginUser_Success() throws Exception {
        UserModel loginRequest = new UserModel();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        UserModel loggedInUser = new UserModel();
        when(userService.loginUser("test@example.com", "password")).thenReturn(loggedInUser);

        ResponseEntity<?> response = userController.loginUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loggedInUser, response.getBody());
    }

    @Test
    void testLoginUser_Failure() throws Exception {
        UserModel loginRequest = new UserModel();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        when(userService.loginUser("test@example.com", "password")).thenThrow(new RuntimeException("Login failed"));

        ResponseEntity<?> response = userController.loginUser(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Login failed", response.getBody());
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");

        UserUpdateDTO updateDTO = new UserUpdateDTO();
        UserModel updatedUser = new UserModel();
        when(userService.updateUser("testuser", updateDTO)).thenReturn(updatedUser);

        ResponseEntity<?> response = userController.updateUser(authentication, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void testUpdateUser_Unauthorized() {
        UserUpdateDTO updateDTO = new UserUpdateDTO();

        ResponseEntity<?> response = userController.updateUser(null, updateDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("No authentication found", response.getBody());
    }

    @Test
    void testShowForgotPasswordForm() {
        assertEquals("forgot-password", userController.showForgotPasswordForm());
    }

    @Test
    void testResetPassword_Success() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("test@example.com");
        request.setNewPassword("newpassword");

        doNothing().when(userService).resetPassword("test@example.com", "newpassword");

        ResponseEntity<?> response = userController.resetPassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password has been reset successfully.", response.getBody());
    }

    @Test
    void testResetPassword_Failure() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("test@example.com");
        request.setNewPassword("newpassword");

        doThrow(new RuntimeException("Reset failed")).when(userService).resetPassword("test@example.com", "newpassword");

        ResponseEntity<?> response = userController.resetPassword(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Reset failed", response.getBody());
    }

    @Test
    void testShowResetSuccessPage() {
        assertEquals("reset-success", userController.showResetSuccessPage());
    }
}