package com.isssr.ticketing_system.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.model.TicketPriority;
import com.isssr.ticketing_system.model.auto_generated.temporary.DataBaseTimeQuery;
import com.isssr.ticketing_system.response_entity.CommonResponseEntity;
import com.isssr.ticketing_system.response_entity.HashMapResponseEntityBuilder;
import com.isssr.ticketing_system.response_entity.JsonViews;
import com.isssr.ticketing_system.response_entity.PageResponseEntityBuilder;
import com.isssr.ticketing_system.service.TicketPriorityService;
import com.isssr.ticketing_system.service.auto_generated.AutoGeneratedTicketService;
import com.isssr.ticketing_system.service.auto_generated.QueryService;
import com.isssr.ticketing_system.service.db_metadata.DBMetadataExtractor;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Validated
@RestController
@RequestMapping(path = "/api/v1/queries")
public class QueryController {

    @Autowired
    private AutoGeneratedTicketService autoGeneratedTicketService;

    @Autowired
    private QueryService queryService;

    @Autowired
    private TicketPriorityService ticketPriorityService;

    @Autowired
    private DBMetadataExtractor dbMetadataExtractor;

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(path = "metadata", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity metadata(@AuthenticationPrincipal Principal principal) {

        Iterable<TicketPriority> priorities = this.ticketPriorityService.findAll();

        return new HashMapResponseEntityBuilder(HttpStatus.OK)
                .set("priorities", StreamSupport.stream(priorities.spliterator(), false).collect(Collectors.toList()))
                .build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity create(@RequestBody DataBaseTimeQuery dataBaseTimeQuery) {

        //try to store new query
        //DataBaseTimeQuery query = queryService.create(dataBaseTimeQuery);

        dataBaseTimeQuery.setQueryPriority(this.ticketPriorityService.findById(dataBaseTimeQuery.getQueryPriority().getId()).get());

        // try to create new query
        try {
            autoGeneratedTicketService.activateQuery(dataBaseTimeQuery);
        } catch (ParseException | SchedulerException e) {
            // set query disabled
            /*dataBaseTimeQuery.disableMe();

            try {
                queryService.updateOne(query.getId(), query);
            } catch (UpdateException | EntityNotFoundException e1) {
                e.printStackTrace();
                //TODO generate ticket here
                return CommonResponseEntity.UnprocessableEntityResponseEntity("FATAL ERROR: " + e1.getMessage());
            }*/
            e.printStackTrace();
            //return CommonResponseEntity.UnprocessableEntityResponseEntity("CREATED BUT NOT INITIALIZED BECAUSE " +e.getMessage());
            return CommonResponseEntity.UnprocessableEntityResponseEntity("NOT CREATED BECAUSE " + e.getMessage());
        }

        this.queryService.create(dataBaseTimeQuery);

        return CommonResponseEntity.OkResponseEntity("CREATED");
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllNotDeletedPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {

        Page<DataBaseTimeQuery> dataBaseTimeQueryPage;
        try {
            dataBaseTimeQueryPage = queryService.findAllNotDeleted(page, pageSize);
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
        return new PageResponseEntityBuilder(dataBaseTimeQueryPage)
                .setStatus(HttpStatus.OK)
                .build();
    }

    @RequestMapping(value = "tables", method = RequestMethod.GET)
    public ResponseEntity getTablesMetadata() {

        try {

            List<String> tableNames = this.dbMetadataExtractor.getTableMetadata();

            return new ResponseEntity<List<String>>(tableNames, HttpStatus.OK);

        } catch (SQLException e) {

            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @RequestMapping(value = "tables/{tableName}/columns", method = RequestMethod.GET)
    public ResponseEntity getTableColumnsMetadata(@PathVariable String tableName) {

        try {

            List<String> columns = this.dbMetadataExtractor.getTableColumns(tableName);

            return new ResponseEntity<List<String>>(columns, HttpStatus.OK);

        } catch (SQLException e) {

            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
