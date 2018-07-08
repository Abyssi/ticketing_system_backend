package com.isssr.ticketing_system.controller;

import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.logger.RecordService;
import com.isssr.ticketing_system.logger.entity.Record;
import com.isssr.ticketing_system.response_entity.CommonResponseEntity;
import com.isssr.ticketing_system.response_entity.PageResponseEntityBuilder;
import com.isssr.ticketing_system.response_entity.ResponseEntityBuilder;
import com.isssr.ticketing_system.utils.PageableUtils;
import com.isssr.ticketing_system.validator.RecordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/v1/records/")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private PageableUtils pageableUtils;

    private RecordValidator recordValidator;

    @Autowired
    public RecordController(RecordValidator recordValidator) {
        this.recordValidator = recordValidator;
    }

    @InitBinder
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(recordValidator);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity get(@PathVariable Integer id) {
        Optional<Record> record = recordService.findById(id);

        if (!record.isPresent())
            return CommonResponseEntity.NotFoundResponseEntity("RECORD_NOT_FOUND");

        return new ResponseEntityBuilder<>(record.get()).setStatus(HttpStatus.OK).build();
    }

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

    @RequestMapping(path = "search/{tag}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity searchByTagPaginated(@PathVariable String tag, @RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            List<Record> recordList = recordService.getRecordsByTag(tag);
            Pageable pageable = pageableUtils.instantiatePageableObject(page, pageSize, null);

            int start = (int) pageable.getOffset();
            int end = (start + pageable.getPageSize()) > recordList.size() ? recordList.size() : (start + pageable.getPageSize());
            Page<Record> recordPage = new PageImpl<>(recordList.subList(start, end), pageable, recordList.size());

            return new PageResponseEntityBuilder(recordPage).setStatus(HttpStatus.OK).build();
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
    }


}
