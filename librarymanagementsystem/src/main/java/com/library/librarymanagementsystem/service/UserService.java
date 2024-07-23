package com.library.librarymanagementsystem.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.librarymanagementsystem.entities.User;
import com.library.librarymanagementsystem.model.UserModel;
import com.library.librarymanagementsystem.model.UserUpdateDTO;
import com.library.librarymanagementsystem.repository.UserRepository;

@Service
@Primary
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel registerUser(UserModel userModel) {
        // Check if user already exists
        if (userRepository.findByEmail(userModel.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        // Check if username already exists
        if (userRepository.findByUsername(userModel.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        // Create new user entity
        User user = new User();
        user.setUsername(userModel.getUsername());
        user.setEmail(userModel.getEmail());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole(User.UserRole.PATRON); // Default role
        user.setCreatedAt(LocalDateTime.now());

        // Save user to database
        User savedUser = userRepository.save(user);

        // Convert saved user back to UserModel
        return convertToUserModel(savedUser);
    }

    public UserModel loginUser(String userName, String password) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Update last login time
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return convertToUserModel(user);
    }

    @Transactional
    public UserModel updateUser(String username, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userUpdateDTO.getFirstName() != null) {
            user.setFirstName(userUpdateDTO.getFirstName());
        }
        if (userUpdateDTO.getLastName() != null) {
            user.setLastName(userUpdateDTO.getLastName());
        }
        if (userUpdateDTO.getEmail() != null) {
            // You might want to add validation here to ensure the new email is unique
            user.setEmail(userUpdateDTO.getEmail());
        }
        // Add other fields as necessary

        User updatedUser = userRepository.save(user);
        return convertToUserModel(updatedUser);
    }

    private UserModel convertToUserModel(User user) {
        UserModel userModel = new UserModel();
        userModel.setId(user.getId());
        userModel.setUsername(user.getUsername());
        userModel.setEmail(user.getEmail());
        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setRole(user.getRole());
        userModel.setCreatedAt(user.getCreatedAt());
        userModel.setLastLogin(user.getLastLogin());
        // Don't set the password in the model
        return userModel;
    }

    public void resetPassword(String email, String newPassword) throws Exception {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new Exception("No user found with this email address"));
        
        // In a real application, you should hash the password before saving
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            return user;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
