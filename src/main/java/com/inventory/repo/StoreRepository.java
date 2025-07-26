package com.inventory.repo;

import com.inventory.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByNameIgnoreCaseAndActiveTrue(String name);
    Optional<Store> findByIdAndActiveTrue(Long id);
}
