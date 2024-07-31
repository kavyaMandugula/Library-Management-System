package com.library.librarymanagementsystem.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.library.librarymanagementsystem.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByUsername(String username);

    List<User> findByUsernameContainingOrEmailContaining(String username, String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.loans WHERE u.id = :id")
    Optional<User> findByIdWithLoans(@Param("id") Long id);
}
