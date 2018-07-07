package com.isssr.ticketing_system.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.model.Target;
import com.isssr.ticketing_system.response_entity.CommonResponseEntity;
import com.isssr.ticketing_system.response_entity.JsonViews;
import com.isssr.ticketing_system.response_entity.PageResponseEntityBuilder;
import com.isssr.ticketing_system.response_entity.ResponseEntityBuilder;
import com.isssr.ticketing_system.service.TargetService;
import com.isssr.ticketing_system.validator.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/v1/targets/")
public class TargetController {

    @Autowired
    private TargetService targetService;

    private ProductValidator productValidator;

    @Autowired
    public TargetController(ProductValidator productValidator) {
        this.productValidator = productValidator;
    }

    @InitBinder
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(productValidator);
    }

    @JsonView(JsonViews.DetailedTarget.class)
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity create(@Valid @RequestBody Target target) {
        targetService.save(target);
        return CommonResponseEntity.OkResponseEntity("CREATED");
    }

    @JsonView(JsonViews.DetailedTarget.class)
    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity get(@PathVariable Long id) {
        Optional<Target> product = targetService.findById(id);

        if (!product.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("TARGET_NOT_FOUND");

        return new ResponseEntityBuilder<>(product.get()).setStatus(HttpStatus.OK).build();
    }

    @JsonView(JsonViews.DetailedTarget.class)
    @RequestMapping(path = "{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestBody Target target) {
        try {
            targetService.updateById(id, target);
        } catch (EntityNotFoundException e) {
            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());
        }

        return CommonResponseEntity.OkResponseEntity("UPDATED");
    }

    @JsonView(JsonViews.DetailedTarget.class)
    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity delete(@PathVariable Long id) {
        if (targetService.deleteById(id))
            return CommonResponseEntity.OkResponseEntity("DELETED");
        else
            return CommonResponseEntity.NotFoundResponseEntity("TARGET_NOT_FOUND");
    }

    @JsonView(JsonViews.DetailedTarget.class)
    @RequestMapping(path = "restore/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity restore(@PathVariable Long id) {
        try {
            Target restoredTarget = this.targetService.restoreById(id);
            return new ResponseEntityBuilder<>(restoredTarget).setStatus(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(value = "all", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<Target> productPage = targetService.findAll(page, pageSize);
            return new PageResponseEntityBuilder(productPage).setStatus(HttpStatus.OK).build();
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllNotDeletedPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<Target> productPage = targetService.findAllNotDeleted(page, pageSize);
            return new PageResponseEntityBuilder(productPage).setStatus(HttpStatus.OK).build();
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(value = "deleted", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllDeletedPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<Target> productPage = targetService.findAllDeleted(page, pageSize);
            return new PageResponseEntityBuilder(productPage).setStatus(HttpStatus.OK).build();
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }

    @JsonView(JsonViews.DetailedTarget.class)
    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity deleteAll() {
        Long count = targetService.count();

        if (count == 0)
            return CommonResponseEntity.NotFoundResponseEntity("TARGETS_NOT_FOUND");

        targetService.deleteAll();

        return CommonResponseEntity.OkResponseEntity("DELETED");
    }
}
