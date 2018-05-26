package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p where p.deleted = false")
    Page<Product> findAllNotDeleted(Pageable pageable);

    @Query("SELECT p FROM Product p where p.deleted = true")
    Page<Product> findAllDeleted(Pageable pageable);
}
