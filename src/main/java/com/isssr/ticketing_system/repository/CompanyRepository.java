package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
    Optional<Company> findByName(String name);

    boolean existsByName(String name);

    boolean existsByDomain(String domain);

}