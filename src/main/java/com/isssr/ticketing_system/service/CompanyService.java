package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.Company;
import com.isssr.ticketing_system.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    public Company save(Company company) {
        return this.companyRepository.save(company);
    }

    @Transactional
    public Optional<Company> findByName(String name) {
        return this.companyRepository.findByName(name);
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.companyRepository.existsByName(name);
    }

    @Transactional
    public boolean existsByDomain(String domain) {
        return this.companyRepository.existsByDomain(domain);
    }
}
