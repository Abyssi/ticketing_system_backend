package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.Privilege;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {
    Optional<Privilege> findByName(String name);

    boolean existsByName(String name);
}
