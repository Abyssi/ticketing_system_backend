package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.Product;
import com.isssr.ticketing_system.response_entity.CommonResponseEntity;
import com.isssr.ticketing_system.response_entity.ListObjectResponseEntityBuilder;
import com.isssr.ticketing_system.response_entity.ObjectResponseEntityBuilder;
import com.isssr.ticketing_system.service.ProductService;
import com.isssr.ticketing_system.validator.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Validated
@RestController
@RequestMapping("/api/v1/products/")
public class ProductController {
    @Autowired
    private ProductService productService;

    private ProductValidator productValidator;

    @Autowired
    public ProductController(ProductValidator productValidator) {
        this.productValidator = productValidator;
    }

    @InitBinder
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(productValidator);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity create(@Valid @RequestBody Product product) {
        productService.save(product);

        return CommonResponseEntity.OkResponseEntity("CREATED");
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getByID(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);

        if (!product.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("PRODUCT_NOT_FOUND");

        return new ObjectResponseEntityBuilder<>(product.get(), "full").setStatus(HttpStatus.OK).build();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody Product product) {
        /*Optional<Product> foundProduct = productService.findById(id);

        if (!foundProduct.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("PRODUCT_NOT_FOUND");

        product.setId(foundProduct.get().getId());
        productService.save(product);*/

        try {

            productService.updateOne(id, product);

        } catch (UpdateException e) {

            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());

        } catch (com.isssr.ticketing_system.exception.EntityNotFoundException e) {

            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());

        }

        return CommonResponseEntity.OkResponseEntity("UPDATED");
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity delete(@PathVariable Long id) {
        /*Optional<Product> foundProduct = productService.findById(id);

        if (!foundProduct.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("PRODUCT_NOT_FOUND");*/

        if (productService.deleteById(id)) {

            return CommonResponseEntity.OkResponseEntity("DELETED");

        } else {

            return CommonResponseEntity.NotFoundResponseEntity("PRODUCT_NOT_FOUND");

        }
    }
    //TODO: AGGIUSTARE!
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllPaginated(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) {
        Stream<Product> products;
        if (page != null && size != null) {
            try {
                products = (productService.findAll(page, size).stream());
            } catch (PageableQueryException e) {
                return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
            } catch (EntityNotFoundException e) {
                return CommonResponseEntity.NotFoundResponseEntity("PRODUCTS_NOT_FOUND");
            }
        } else
            products = (StreamSupport.stream(productService.findAll().spliterator(), false));

        return new ListObjectResponseEntityBuilder<>(products.collect(Collectors.toList()))
                .setStatus(HttpStatus.OK)
                .build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity deleteAll() {
        Long count = productService.count();

        if (count == 0)
            return CommonResponseEntity.NotFoundResponseEntity("PRODUCTS_NOT_FOUND");

        productService.deleteAll();

        return CommonResponseEntity.OkResponseEntity("DELETED");
    }
}
