package com.uit.realestate.repository.user;

import com.uit.realestate.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> findByUsernameContainingOrFullNameContainingOrEmailContaining(String username, String fullName, String email, Pageable pageable);
}

