package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);

    boolean existsByName(String name);
}
