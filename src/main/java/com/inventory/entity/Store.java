package com.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "stores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String location;

    private String managerName;

    private String contactNumber;

    private boolean active = true;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<Product> products;  // <-- NEW: Products available in this store
}
