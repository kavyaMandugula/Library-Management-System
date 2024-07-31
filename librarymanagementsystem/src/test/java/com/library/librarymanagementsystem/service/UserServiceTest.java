package com.library.librarymanagementsystem.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.library.librarymanagementsystem.entities.User;
import com.library.librarymanagementsystem.model.UserModel;
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

    // Existing test methods...

    @Test
    void testSearchUsers() {
        String query = "test";
        List<User> mockUsers = Arrays.asList(new User(), new User());

        when(userRepository.findByUsernameContainingOrEmailContaining(query, query)).thenReturn(mockUsers);

        List<User> result = userService.searchUsers(query);

        assertEquals(2, result.size());
        verify(userRepository).findByUsernameContainingOrEmailContaining(query, query);
    }

    @Test
    void testGetAllUsers() {
        List<User> mockUsers = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserModel> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserById_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findByIdWithLoans(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void testGetUserById_UserNotFound() {
        Long userId = 1L;

        when(userRepository.findByIdWithLoans(userId)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserById(userId));
    }
}