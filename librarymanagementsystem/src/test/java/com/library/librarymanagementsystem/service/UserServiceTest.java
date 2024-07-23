package com.library.librarymanagementsystem.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.library.librarymanagementsystem.entities.User;
import com.library.librarymanagementsystem.model.UserModel;
import com.library.librarymanagementsystem.model.UserUpdateDTO;
import com.library.librarymanagementsystem.repository.UserRepository;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        UserModel userModel = new UserModel();
        userModel.setUsername("testuser");
        userModel.setEmail("test@example.com");
        userModel.setPassword("password");
        userModel.setFirstName("Test");
        userModel.setLastName("User");

        when(userRepository.findByEmail(userModel.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(userModel.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userModel.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        UserModel result = userService.registerUser(userModel);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals(User.UserRole.PATRON, result.getRole());
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        UserModel userModel = new UserModel();
        userModel.setEmail("existing@example.com");

        when(userRepository.findByEmail(userModel.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> userService.registerUser(userModel));
    }

    @Test
    void testLoginUser_Success() {
        String username = "testuser";
        String password = "password";

        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);

        UserModel result = userService.loginUser(username, password);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository).save(any(User.class)); // Verify last login time is updated
    }

    @Test
    void testLoginUser_InvalidPassword() {
        String username = "testuser";
        String password = "wrongpassword";

        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.loginUser(username, password));
    }

    @Test
    void testUpdateUser_Success() {
        String username = "testuser";
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setFirstName("NewFirstName");
        updateDTO.setLastName("NewLastName");

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserModel result = userService.updateUser(username, updateDTO);

        assertNotNull(result);
        assertEquals("NewFirstName", result.getFirstName());
        assertEquals("NewLastName", result.getLastName());
    }

    @Test
    void testResetPassword_Success() throws Exception {
        String email = "test@example.com";
        String newPassword = "newpassword";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        userService.resetPassword(email, newPassword);

        verify(userRepository).save(user);
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void testResetPassword_UserNotFound() {
        String email = "nonexistent@example.com";
        String newPassword = "newpassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> userService.resetPassword(email, newPassword));
    }

    @Test
    void testGetUserByUsername_Success() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testGetUserByUsername_UserNotFound() {
        String username = "nonexistent";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByUsername(username));
    }

    @Test
    void testLoadUserByUsername_Success() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String username = "nonexistent";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }
}