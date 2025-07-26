package com.inventory.repo;

import com.inventory.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByNameIgnoreCaseAndActiveTrue(String name);
    Optional<Supplier> findByIdAndActiveTrue(Long id);
}
