package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.model.Product;
import com.isssr.ticketing_system.repository.ProductRepository;
import com.isssr.ticketing_system.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PageableUtils pageableUtils;

    @Transactional
    public Product save(Product product) {
        if (product.getId() == null && this.productRepository.existsByName(product.getName()))
            product.setId(this.findByName(product.getName()).get().getId());
        return this.productRepository.save(product);
    }

    @Transactional
    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.productRepository.existsById(id);
    }

    @Transactional
    public Iterable<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Transactional
    public Iterable<Product> findAllById(Iterable<Long> ids) {
        return this.productRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.productRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.productRepository.existsById(id);
        if (exists) this.productRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.productRepository.deleteAll();
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.productRepository.existsByName(name);
    }

    @Transactional
    public Optional<Product> findByName(String name) {
        return this.productRepository.findByName(name);
    }

    @Transactional
    public Page<Product> findAll(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    @Transactional
    public Page<Product> findAll(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException, EntityNotFoundException {
        Page<Product> retrievedPage = this.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }
}
