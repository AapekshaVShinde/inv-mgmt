package com.inventory.repo;

import com.inventory.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.BitSet;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndActiveTrue(String emailOrUsername);

    Optional<User> findByUsernameAndActiveTrue(String emailOrUsername);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<User> findByActiveTrue(Pageable pageable);
}
