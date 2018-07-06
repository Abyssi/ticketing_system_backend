package com.isssr.ticketing_system.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.logger.RecordService;
import com.isssr.ticketing_system.logger.entity.Record;
import com.isssr.ticketing_system.response_entity.CommonResponseEntity;
import com.isssr.ticketing_system.response_entity.JsonViews;
import com.isssr.ticketing_system.response_entity.PageResponseEntityBuilder;
import com.isssr.ticketing_system.response_entity.ResponseEntityBuilder;
import com.isssr.ticketing_system.validator.RecordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/v1/records/")
public class RecordController {

    @Autowired
    private RecordService recordService;

    private RecordValidator recordValidator;

    @Autowired
    public RecordController(RecordValidator recordValidator) {
        this.recordValidator = recordValidator;
    }

    @InitBinder
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(recordValidator);
    }

    @JsonView(JsonViews.DetailedRecord.class)
    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity get(@PathVariable Integer id) {
        Optional<Record> record = recordService.findById(id);

        if (!record.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("RECORD_NOT_FOUND");

        return new ResponseEntityBuilder<>(record.get()).setStatus(HttpStatus.OK).build();
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            Page<Record> recordPage = recordService.findAll(page, pageSize);
            return new PageResponseEntityBuilder(recordPage).setStatus(HttpStatus.OK).build();
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }


}
