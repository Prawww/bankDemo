package com.example.bankDemo.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByAccountNumber(String account);

    Optional<User> findByEmail(String email);

    User findByAccountNumber(String account);

    Page<User> findByEmail(String email, Pageable pageable);

    Page<User> findAllByEmail(String email, Pageable pageable);
}
