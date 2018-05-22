package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.Visibility;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisibilityRepository extends CrudRepository<Visibility, Long> {
    Optional<Visibility> findByName(String name);

    boolean existsByName(String name);
}
