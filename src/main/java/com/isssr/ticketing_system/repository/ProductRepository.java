package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    Optional<Product> findByName(String name);

    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable);
}
